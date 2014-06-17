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

import java.util.Map;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public interface Entity {

    /**
     * Collection the {@link Entity} is part of. Must validate the following
     * regex: [a-zA-Z0-9-/]+
     *
     * @return
     */
    String getCollection();

    /**
     * GUID. Automatically generated by Java. Example:
     * 751f0ac8-c6fe-4adb-b75c-e211e4462b76
     *
     * @return
     */
    String getId();

    /**
     * Tracks the version of the {@link Entity}. This is automatically updated
     * whenever the {@link Entity} is created or updated.
     *
     * @return
     */
    Long getVersion();

    Map<String, String> getProperties();

    Entity setProperties(Map<String, String> properties);
}
