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
import com.akamai.open.moonlighting.persistence.service.impl.util.EntityCreator;
import com.akamai.open.moonlighting.persistence.service.impl.util.TestConstants;
import com.akamai.open.moonlighting.persistence.service.impl.util.UuidGenerator;
import com.akamai.open.moonlighting.rdbms.connection.service.RdbmsConnectionService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class EntityDaoTest extends TestCase {

    private static final String WORKSPACE = TestConstants.WORKSPACE;
    private EntityDao dao;

    @Override
    protected void setUp() throws Exception {
        final RdbmsConnectionService dbConnectionService = new InMemoryRdbmsConnectionServiceImpl(UuidGenerator.generateUuid());
        dao = new EntityDao(WORKSPACE, dbConnectionService, true);
    }

    @Override
    protected void tearDown() throws Exception {
        dao = null;
    }

    public void testCreate() throws Exception {
        System.out.print("testCreate ... ");

        final int n = 10;
        for (int i = 0; i < n; i++) {
            dao.create(EntityCreator.createEntity());
        }
        long size = dao.selectAll().size();
        assertEquals(size, n);

        System.out.println("OK");
    }

    public void testCreateSameEntity() throws Exception {
        System.out.print("testCreateSameEntity ... ");
        final int n = 100;

        // create the first time
        final Entity e = dao.create(EntityCreator.createEntity());

        for (int i = 0; i < n; i++) {
            // should be able to create the same entity multiple times
            dao.create(e);
        }
        assertTrue(dao.selectAll().size() >= n + 1);
        System.out.println("OK");
    }

    public void testReadEntity() throws Exception {
        System.out.print("testReadEntity ... ");

        // first store a test entity
        final Entity e = dao.create(EntityCreator.createEntity());

        // and then make sure we can look it up
        final Entity result = dao.read(e.getCollection(), e.getId());

        assertNotNull(result);
        assertEquals(result.getCollection(), e.getCollection());
        assertEquals(result.getId(), e.getId());
        assertEquals(result.getProperties(), e.getProperties());
        System.out.println("OK");
    }

    public void testReadEntityDoesNotExist() throws Exception {
        System.out.print("testReadEntityDoesNotExist ... ");

        // create a new entity
        final Entity e = EntityCreator.createEntity();
        // and then try to look it up
        final Entity result = dao.read(e.getCollection(), e.getId());

        assertNull(result);
        System.out.println("OK");
    }

    public void testUpdateEntity() throws Exception {
        System.out.print("testUpdateEntity ... ");
        final String newPropertyName = "newPropertyName";

        // create and persist a new entity
        final Entity originalEntity = dao.create(EntityCreator.createEntity());

        final int count = dao.selectAll().size();
        final int n = 10;

        // make a bunch of updates and make sure it adds a new row and updates properties
        for (int i = 0; i < n; i++) {
            // get the latest version of the entity first
            final Entity e = dao.read(originalEntity.getCollection(), originalEntity.getId());

            // change entity property
            Map<String, String> props = e.getProperties();
            props.put("now", new Date().toString());
            props.put(newPropertyName, "whatever");
            e.setProperties(props);

            // update
            final Entity result = dao.update(e);
            assertNotNull(result);
            assertEquals(result.getCollection(), e.getCollection());
            assertEquals(result.getId(), e.getId());
            assertEquals(result.getProperties(), props);
            assertNotSame(e, result);
            assertTrue(e.getVersion() < result.getVersion());
        }
        assertEquals(dao.selectAll().size(), count + n);
        System.out.println("OK");
    }

    public void testUpdateEntityDoesNotExist() throws Exception {
        System.out.print("testUpdateEntityDoesNotExist ... ");
        final String newPropertyName = "newPropertyName";

        // create a transient entity
        final Entity e = EntityCreator.createEntity();

        // change entity property
        Map<String, String> props = e.getProperties();
        props.put("now", new Date().toString());
        props.put(newPropertyName, "whatever");
        e.setProperties(props);

        try {
            // update
            dao.update(e);
            fail("Cannot delete an Entity that does not exist");
        } catch (EntityNotFoundException ex) {
            System.out.println("OK");
        }
    }

    public void testUpdateStaleEntity() throws Exception {
        System.out.print("testUpdateStaleEntity ... ");

        // create the original entity
        Entity originalEntity = dao.create(EntityCreator.createEntity());

        // make a bunch of updates to the original entity
        makeABunchOfUpdates(originalEntity, 4);

        // now try to update the original entity; 
        // this should fail as it is stale
        try {
            dao.update(originalEntity);
            fail("Cannot update stale entity");
        } catch (StaleEntityException ex) {
            System.out.println("OK");
        }
    }

    public void testDeleteEntity() throws Exception {
        System.out.print("testDeleteEntity ... ");
        // create random entities to populate the table
        dao.create(EntityCreator.createEntity());
        dao.create(EntityCreator.createEntity());
        dao.create(EntityCreator.createEntity());

        // save it first
        final Entity originalEntity = dao.create(EntityCreator.createEntity());

        // should be able to find it
        assertNotNull(dao.read(originalEntity.getCollection(), originalEntity.getId()));

        final int n = 4;
        // make a bunch of updates to the original entity
        makeABunchOfUpdates(originalEntity, n);

        // count how many entities exist
        final int count = dao.selectAll().size();

        // delete it now
        dao.delete(dao.read(originalEntity.getCollection(), originalEntity.getId()));

        // should not be able to find it
        assertNull(dao.read(originalEntity.getCollection(), originalEntity.getId()));
        assertEquals(dao.selectAll().size(), count - (n + 1));

        System.out.println("OK");
    }

    public void testDeleteStaleEntity() throws Exception {
        System.out.print("testDeleteStaleEntity ... ");

        // create the original entity
        Entity originalEntity = dao.create(EntityCreator.createEntity());

        // make a bunch of updates to the original entity
        makeABunchOfUpdates(originalEntity, 4);

        // now try to update the original entity; 
        // this should fail as it is stale
        try {
            dao.delete(originalEntity);
            fail("Cannot update stale entity");
        } catch (StaleEntityException ex) {
            System.out.println("OK");
        }
    }

    public void testSelectAll() throws Exception {
        System.out.print("testSelectAll ... ");
        final int n = 10;
        // create a few test entities
        for (int i = 0; i < n; i++) {
            dao.create(EntityCreator.createEntity());
        }

        final List<Entity> entities = dao.selectAll();
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
        // there should be at least n items
        assertTrue(entities.size() >= n);

        System.out.println("OK");
    }

    private void makeABunchOfUpdates(final Entity originalEntity, int n) throws SQLException,
            IOException, EntityNotFoundException, StaleEntityException {
        // make a bunch of updates to the original entity
        for (int i = 0; i < n; i++) {
            final Entity e = dao.read(originalEntity.getCollection(), originalEntity.getId());
            Map<String, String> props = e.getProperties();
            props.put("now", new Date().toString());
            e.setProperties(props);
            dao.update(e);
        }
    }
}
