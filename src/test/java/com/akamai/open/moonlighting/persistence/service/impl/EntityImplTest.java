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
import com.akamai.open.moonlighting.persistence.service.impl.util.EntityCreator;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class EntityImplTest extends TestCase {

    public void testGetPropertiesAsJson() throws Exception {
        System.out.print("testGetPropertiesAsJson ... ");
        final Entity e = EntityCreator.createEntity();
        final String json = EntityCreator.getPropertiesAsJson(e);
        assertNotNull(json);
        assertFalse(json.isEmpty());

        // transitive conversion check
        assertEquals(e.getProperties(), EntityCreator.setPropertiesFromJson(e, json).getProperties());
        System.out.println("OK");
    }

    public void testSetPropertiesFromJson() throws Exception {
        System.out.print("testSetPropertiesFromJson ... ");
        final Entity e = EntityCreator.createEntity();
        final Map<String, String> oldProperties = e.getProperties();

        final String json = EntityCreator.getPropertiesAsJson(e);
        EntityCreator.setPropertiesFromJson(e, json);
        final Map<String, String> newProperties = e.getProperties();
        assertNotNull(newProperties);
        assertFalse(newProperties.isEmpty());
        assertEquals(newProperties, oldProperties);

        System.out.println("OK");
    }

    public void testGetVersion() throws Exception {
        System.out.print("testGetVersion ... ");
        final long now = 1398355766000L;
        final int nanos = 123456000;
        final Timestamp ts = new Timestamp(now);
        ts.setNanos(nanos);

        EntityImpl e = new EntityImpl(EntityCreator.TYPE, "bar", Collections.EMPTY_MAP, ts);
        assertNotNull(e.getVersion());
        assertEquals(e.getVersion(), Long.valueOf("1398355766123456000"));
        System.out.println("OK");
    }

}
