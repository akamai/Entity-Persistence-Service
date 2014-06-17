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
package com.akamai.open.moonlighting.persistence.service.impl.util;

import com.akamai.open.moonlighting.persistence.service.Entity;
import com.akamai.open.moonlighting.persistence.service.impl.EntityImpl;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class EntityCreator {

    public static final String TYPE = "penguin";
    private static final JsonUtil jsonUtil = new JsonUtil();

    public static Entity createEntity() {
        Map<String, String> props = new HashMap<>();
        props.put("foo", "bar");
        props.put("baz", "qux");
        props.put("now", new Date().toString());

        return new EntityImpl(TYPE, UuidGenerator.generateUuid(), props);
    }

    public static String getPropertiesAsJson(Entity e) throws IOException {
        return jsonUtil.toJson(e.getProperties());
    }

    public static Entity setPropertiesFromJson(Entity e, String json) throws IOException {
        Map<String, String> newProperties = jsonUtil.toMap(json);
        return e.setProperties(newProperties);
    }
}
