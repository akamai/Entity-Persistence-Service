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

import java.io.IOException;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class JsonUtil {

    private static final ObjectMapper om = new ObjectMapper();

    public String toJson(final Map<String, String> map) throws IOException {
        return om.writeValueAsString(map);
    }

    public Map<String, String> toMap(final String json) throws IOException {
        return om.readValue(json, new TypeReference<Map<String, String>>() {
        });
    }
}
