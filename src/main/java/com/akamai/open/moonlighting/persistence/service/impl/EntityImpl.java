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
import com.akamai.open.moonlighting.persistence.service.impl.util.MonikerValidator;
import com.akamai.open.moonlighting.persistence.service.impl.util.TimestampUtil;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class EntityImpl implements Entity {

    private final String id;
    private final String collection;
    private final Map<String, String> properties = new HashMap<>();
    private Long version;
    // --
    private final TimestampUtil timestampUtil = new TimestampUtil();
    private final MonikerValidator monikerValidator = new MonikerValidator();

    public EntityImpl(String collection, String id) {
        this.collection = monikerValidator.validate(collection);
        this.id = monikerValidator.validate(id);
        this.version = -1L;
    }

    public EntityImpl(String collection, String id, Map<String, String> properties) {
        this(collection, id);
        setProperties(properties);
    }

    public EntityImpl(String collection, String id, Map<String, String> properties, 
            Timestamp updated) throws IOException {
        this(collection, id, properties);
        // convert timestamp to nanoseconds
        this.version = timestampUtil.toLong(updated);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public final Entity setProperties(Map<String, String> properties) {
        this.properties.clear();
        this.properties.putAll(properties);
        return this;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "id: " + getId() + "; collection: " + getCollection()
                + "; version: " + getVersion() + "; properties: " + getProperties();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.collection);
        hash = 71 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityImpl other = (EntityImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.collection, other.collection)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

}
