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
package com.akamai.open.moonlighting.persistence.service;

import com.akamai.open.moonlighting.persistence.service.exception.EntityExistsException;
import com.akamai.open.moonlighting.persistence.service.exception.EntityNotFoundException;
import com.akamai.open.moonlighting.persistence.service.exception.StaleEntityException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public interface Workspace {

    /**
     * Create a new {@link Entity} in the specified collection with the
     * specified properties. By default, all properties in the <code>Map</code>
     * will be indexed.
     *
     * @param collection
     * @param properties
     * @return
     * @throws PersistenceServiceException
     * @throws EntityExistsException
     */
    Entity create(String collection, Map<String, String> properties)
            throws PersistenceServiceException, EntityExistsException;

    /**
     * Create a new {@link Entity} in the specified collection with the
     * specified properties. All properties in the {@link IndexConfiguration}
     * will be indexed.
     *
     * @param collection
     * @param properties
     * @param indexConfiguration
     * @return
     * @throws PersistenceServiceException
     * @throws EntityExistsException
     */
    Entity create(String collection, Map<String, String> properties,
            IndexConfiguration indexConfiguration) throws PersistenceServiceException,
            EntityExistsException;

    /**
     * Returns the latest version of an {@link Entity} that is part of the
     * specified collection and has the specified id. Returns null otherwise.
     *
     * @param collection
     * @param id
     * @return
     * @throws PersistenceServiceException
     */
    Entity read(String collection, String id) throws PersistenceServiceException;

    /**
     * Updates an existing {@link Entity} by creating and persisting a new
     * version of itself. All properties in the {@link IndexConfiguration} will
     * be indexed. Will throw {@link StaleEntityException} if the {@link Entity}
     * that's being updated is not the latest version.
     *
     * @param e
     * @return
     * @throws PersistenceServiceException
     * @throws EntityNotFoundException
     * @throws StaleEntityException
     */
    Entity update(Entity e) throws PersistenceServiceException,
            EntityNotFoundException, StaleEntityException;

    /**
     * Updates an existing {@link Entity} by creating and persisting a new
     * version of itself. By default, all properties in the <code>Map</code>
     * will be indexed. Will throw {@link StaleEntityException} if the
     * {@link Entity} that's being updated is not the latest version.
     *
     * @param e
     * @param indexConfiguration
     * @return
     * @throws PersistenceServiceException
     * @throws EntityNotFoundException
     * @throws StaleEntityException
     */
    Entity update(Entity e, IndexConfiguration indexConfiguration)
            throws PersistenceServiceException, EntityNotFoundException, StaleEntityException;

    /**
     * Deletes (logical delete) an {@link Entity}. Will throw
     * {@link StaleEntityException} if the {@link Entity} that's being deleted
     * is not the latest version.
     *
     * @param e
     * @throws PersistenceServiceException
     * @throws EntityNotFoundException
     * @throws StaleEntityException
     */
    void delete(Entity e) throws PersistenceServiceException,
            EntityNotFoundException, StaleEntityException;

    /**
     * Returns a list of {@link Entity} in the specified collection
     *
     * @param collection
     * @return
     * @throws PersistenceServiceException
     */
    List<Entity> list(final String collection) throws PersistenceServiceException;

}
