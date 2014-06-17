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
package com.akamai.open.moonlighting.persistence.service.impl;

import com.akamai.open.moonlighting.persistence.service.Entity;
import com.akamai.open.moonlighting.persistence.service.IndexConfiguration;
import com.akamai.open.moonlighting.persistence.service.PersistenceServiceException;
import com.akamai.open.moonlighting.persistence.service.Workspace;
import com.akamai.open.moonlighting.persistence.service.exception.EntityExistsException;
import com.akamai.open.moonlighting.persistence.service.exception.EntityNotFoundException;
import com.akamai.open.moonlighting.persistence.service.exception.StaleEntityException;
import com.akamai.open.moonlighting.persistence.service.impl.dao.EntityDao;
import com.akamai.open.moonlighting.persistence.service.impl.util.MonikerValidator;
import com.akamai.open.moonlighting.rdbms.connection.service.RdbmsConnectionService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class WorkspaceImpl implements Workspace {

    private static final Logger LOG = Logger.getLogger(WorkspaceImpl.class);
    private final RdbmsConnectionService dbConnectionService;
    private final String workspaceName;
    // --
    private final MonikerValidator monikerValidator;

    public WorkspaceImpl(String workspaceName, RdbmsConnectionService dbConnectionService) {
        this.workspaceName = workspaceName;
        this.dbConnectionService = dbConnectionService;
        this.monikerValidator = new MonikerValidator();
    }

    @Override
    public Entity create(String collection, Map<String, String> properties)
            throws PersistenceServiceException, EntityExistsException {
        return create(collection, properties, new IndexConfiguration(properties.keySet()));
    }

    @Override
    public Entity create(String collection, Map<String, String> properties,
            IndexConfiguration indexConfiguration) throws PersistenceServiceException,
            EntityExistsException {
        collection = parseType(collection);

        // save
        EntityDao dao = null;
        try {
            dao = new EntityDao(workspaceName, dbConnectionService);
            return dao.create(collection, properties, indexConfiguration.getColumnsToIndex());
        } catch (IOException | SQLException ex) {
            final String msg = "Could not create Entity: " + collection + ":" + properties;
            LOG.error(msg, ex);
            throw new PersistenceServiceException(msg, ex);
        } finally {
            try {
                if (dao != null) {
                    dao.close();
                }
            } catch (SQLException ex) {
                LOG.warn("Could not close connection", ex);
            }
        }
    }

    @Override
    public Entity read(String collection, final String id) throws PersistenceServiceException {
        collection = parseType(collection);

        EntityDao dao = null;
        try {
            dao = new EntityDao(workspaceName, dbConnectionService);
            return dao.read(collection, id);
        } catch (IOException | SQLException ex) {
            final String msg = "Could not read Entity: " + collection + ":" + id;
            LOG.error(msg, ex);
            throw new PersistenceServiceException(msg, ex);
        } finally {
            try {
                if (dao != null) {
                    dao.close();
                }
            } catch (SQLException ex) {
                LOG.warn("Could not close connection", ex);
            }
        }
    }

    @Override
    public Entity update(Entity e) throws PersistenceServiceException,
            EntityNotFoundException, StaleEntityException {
        return update(e, new IndexConfiguration(e.getProperties().keySet()));
    }

    @Override
    public Entity update(Entity e, IndexConfiguration indexConfiguration)
            throws PersistenceServiceException, EntityNotFoundException, StaleEntityException {
        EntityDao dao = null;
        try {
            dao = new EntityDao(workspaceName, dbConnectionService);
            return dao.update(e, indexConfiguration.getColumnsToIndex());
        } catch (IOException | SQLException ex) {
            final String msg = "Could not update Entity: " + e.getCollection() + ":" + e.getId();
            LOG.error(msg, ex);
            throw new PersistenceServiceException(msg, ex);
        } finally {
            try {
                if (dao != null) {
                    dao.close();
                }
            } catch (SQLException ex) {
                LOG.warn("Could not close connection", ex);
            }
        }
    }

    @Override
    public void delete(Entity e) throws PersistenceServiceException,
            EntityNotFoundException, StaleEntityException {
        EntityDao dao = null;
        try {
            dao = new EntityDao(workspaceName, dbConnectionService);
            dao.delete(e);
        } catch (IOException | SQLException ex) {
            final String msg = "Could not delete Entity: " + e.getCollection() + ":" + e.getId();
            LOG.error(msg, ex);
            throw new PersistenceServiceException(msg, ex);
        } finally {
            try {
                if (dao != null) {
                    dao.close();
                }
            } catch (SQLException ex) {
                LOG.warn("Could not close connection", ex);
            }
        }
    }

    @Override
    public List<Entity> list(String collection) throws PersistenceServiceException {
        collection = parseType(collection);

        EntityDao dao = null;
        try {
            dao = new EntityDao(workspaceName, dbConnectionService);
            return dao.list(collection);
        } catch (SQLException ex) {
            final String msg = "Could not find entities matching " + collection;
            LOG.error(msg, ex);
            throw new PersistenceServiceException(msg, ex);
        } finally {
            try {
                if (dao != null) {
                    dao.close();
                }
            } catch (SQLException ex) {
                LOG.warn("Could not close connection", ex);
            }
        }
    }

    private String parseType(String type) {
        return monikerValidator.validate(type);
    }

}
