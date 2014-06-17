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

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class JsonUtilTest extends TestCase {

    private JsonUtil jsonUtil;
    private static final String JSON = "{\"baz\":\"qux\",\"foo\":\"bar\"}";
    private Map<String, String> defaultMap;

    @Override
    protected void setUp() throws Exception {
        jsonUtil = new JsonUtil();
        defaultMap = new HashMap<>();
        defaultMap.put("foo", "bar");
        defaultMap.put("baz", "qux");
    }

    @Override
    protected void tearDown() throws Exception {
        jsonUtil = null;
    }

    public void testToJsonFromMap() throws Exception {
        System.out.print("testToJsonFromMap ... ");

        final String json = jsonUtil.toJson(defaultMap);

        assertNotNull(json);
        assertFalse(json.isEmpty());
        assertEquals(json, JSON);

        System.out.println("OK");
    }

    public void testToMapFromJson() throws Exception {
        System.out.print("testToMapFromJson ... ");

        final Map<String, String> map = jsonUtil.toMap(JSON);

        assertNotNull(map);
        assertEquals(map.size(), defaultMap.size());
        assertEquals(map, defaultMap);

        System.out.println("OK");
    }

}
