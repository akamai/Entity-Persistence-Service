/* 
 * Copyright 2014 Akamai Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.akamai.open.moonlighting.persistence.service.impl.dao;

import com.akamai.open.moonlighting.persistence.service.Entity;
import com.akamai.open.moonlighting.persistence.service.exception.EntityNotFoundException;
import com.akamai.open.moonlighting.persistence.service.exception.StaleEntityException;
import com.akamai.open.moonlighting.persistence.service.impl.EntityImpl;
import com.akamai.open.moonlighting.persistence.service.impl.dao.queries.DeleteEntityById;
import com.akamai.open.moonlighting.persistence.service.impl.dao.queries.DeleteEntityProperties;
import com.akamai.open.moonlighting.persistence.service.impl.dao.queries.InsertEntityProperty;
import com.akamai.open.moonlighting.persistence.service.impl.dao.queries.InsertEntityQuery;
import com.akamai.open.moonlighting.persistence.service.impl.dao.queries.SelectAllEntitiesQuery;
import com.akamai.open.moonlighting.persistence.service.impl.dao.queries.SelectEntitiesByType;
import com.akamai.open.moonlighting.persistence.service.impl.dao.queries.SelectEntityById;
import com.akamai.open.moonlighting.persistence.service.impl.dao.rs.EntityResultSetProcessor;
import com.akamai.open.moonlighting.persistence.service.impl.util.TimestampUtil;
import com.akamai.open.moonlighting.persistence.service.impl.util.UuidGenerator;
import com.akamai.open.moonlighting.rdbms.connection.service.RdbmsConnectionService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class EntityDao {

    private static final Logger LOG = Logger.getLogger(EntityDao.class);

    // ----
    private final SqlHelper helper;
    private final String workspace;
    private final boolean unitTest;

    public EntityDao(final String workspace, final RdbmsConnectionService dbConnectionService)
            throws SQLException {
        this(workspace, dbConnectionService, false);
    }

    public EntityDao(final String workspace, final RdbmsConnectionService dbConnectionService,
            final boolean unitTest)
            throws SQLException {
        this.workspace = workspace;
        this.helper = new SqlHelper(dbConnectionService.getConnection());
        this.unitTest = unitTest;
    }

    public Entity create(final String collection, final Map<String, String> properties,
            final Set<String> columnsToIndex)
            throws SQLException, IOException {
        return create(
                new EntityImpl(collection, UuidGenerator.generateUuid(), properties),
                columnsToIndex);
    }

    public Entity create(final Entity e) throws SQLException, IOException {
        return create(e, e.getProperties().keySet());
    }

    public Entity create(final Entity e, final Set<String> columnsToIndex) throws SQLException, IOException {
        // TODO validate type

        LOG.info("create() " + e.getCollection() + ":" + e.getId());
        // insert new entity
        helper.insert(new InsertEntityQuery(workspace, e));

        Entity savedEntity = read(e.getCollection(), e.getId());
        // update entity properties
        updateEntityProperties(savedEntity, columnsToIndex);
        // commit!
        helper.commit();

        return savedEntity;
    }

    public Entity read(final String collection, final String id) throws SQLException, IOException {
        LOG.info("read() " + collection + ":" + id);
        final EntityResultSetProcessor processor = new EntityResultSetProcessor();
        helper.select(new SelectEntityById(workspace, collection, id, unitTest), processor);

        final List<Entity> result = processor.getResult();
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public Entity update(Entity e) throws IOException, SQLException,
            EntityNotFoundException, StaleEntityException {
        return update(e, e.getProperties().keySet());
    }

    public Entity update(Entity e, final Set<String> columnsToIndex) throws IOException, SQLException,
            EntityNotFoundException, StaleEntityException {
        LOG.info("update() " + e);
        // first read to make sure entity exists
        getExistingEntity(e);
        return create(e, columnsToIndex);
    }

    public void delete(final Entity e) throws SQLException, EntityNotFoundException,
            IOException, StaleEntityException {
        LOG.info("delete() " + e);
        final Entity existingEntity = getExistingEntity(e);
        // delete properties first
        deleteEntityProperties(existingEntity);

        // finally, delete main entity
        helper.delete(new DeleteEntityById(workspace, existingEntity));

        // commit!
        helper.commit();
    }

    public List<Entity> list(String collection) throws SQLException {
        LOG.info("list() " + collection);
        final EntityResultSetProcessor processor = new EntityResultSetProcessor();
        helper.select(new SelectEntitiesByType(workspace, collection), processor);
        return processor.getResult();
    }

    private Entity getExistingEntity(Entity e) throws SQLException, IOException,
            EntityNotFoundException, StaleEntityException {
        // first read to make sure entity exists
        final Entity existingEntity = read(e.getCollection(), e.getId());
        // throw exception if no entity was found; this is usually a logical error
        // on the client side
        if (existingEntity == null) {
            throw new EntityNotFoundException(e);
        }

        // check if entity is stale
        if (!existingEntity.equals(e)) {
            throw new StaleEntityException(e, existingEntity);
        }

        return existingEntity;
    }

    private void deleteEntityProperties(final Entity e) throws SQLException {
        LOG.info("Deleting entityProperties: " + e);

        helper.delete(new DeleteEntityProperties(workspace, e));
    }

    private void addEntityProperties(final Entity e, final Set<String> columnsToIndex)
            throws SQLException {
        // insert all properties in the entity properties table
        helper.insertBatch(new InsertEntityProperty(workspace,
                e.getCollection(),
                e.getId(),
                new TimestampUtil().toTimestamp(e.getVersion()),
                e.getProperties(),
                columnsToIndex));

    }

    private void updateEntityProperties(final Entity e, final Set<String> columnsToIndex)
            throws SQLException {
        // delete properties from entity properties table
        deleteEntityProperties(e);

        if (columnsToIndex != null && !columnsToIndex.isEmpty()) {
            // add new properties
            addEntityProperties(e, columnsToIndex);
        }
    }

    public void close() throws SQLException {
        helper.close();
    }

    /**
     * Returns total number of entities stored. For debugging purposes only
     *
     * @return
     */
    protected List<Entity> selectAll() throws SQLException, IOException {
        final EntityResultSetProcessor processor = new EntityResultSetProcessor();
        helper.select(new SelectAllEntitiesQuery(), processor);
        return processor.getResult();
    }

}
