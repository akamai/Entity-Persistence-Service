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
package com.akamai.open.moonlighting.persistence.service.impl.dao.queries;

import com.akamai.open.moonlighting.persistence.service.Entity;
import com.akamai.open.moonlighting.persistence.service.impl.util.EntityCreator;
import static junit.framework.Assert.assertEquals;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class InsertEntityQueryTest extends BaseEntityQueryTestCase {

    public void testQueryNullEntity() throws Exception {
        System.out.print("testQueryNullEntity ... ");
        try {
            helper.insert(new InsertEntityQuery("test", null));
            fail();
        } catch (IllegalArgumentException ex) {
            System.out.println("OK");
        }
    }

    public void testQuery() throws Exception {
        System.out.print("testQuery ... ");
        // insert new entity
        final Entity e = EntityCreator.createEntity();
        int rowsInserted = helper.insert(new InsertEntityQuery("test", e));

        assertEquals(rowsInserted, 1);
        System.out.println("OK");
    }

}
