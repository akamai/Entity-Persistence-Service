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
package com.akamai.open.moonlighting.persistence.service.exception;

import com.akamai.open.moonlighting.persistence.service.Entity;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class StaleEntityException extends Exception {

    /**
     * Creates a new instance of <code>StaleEntityException</code> without
     * detail message.
     *
     * @param oldEntity
     * @param newEntity
     */
    public StaleEntityException(Entity oldEntity, Entity newEntity) {
        super("You are working on a stale entity. Old Entity: " + oldEntity.toString()
                + "; New Entity: " + newEntity.toString());
    }

}
