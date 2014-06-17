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
import com.akamai.open.moonlighting.persistence.service.impl.dao.rs.EntityResultSetProcessor;
import com.akamai.open.moonlighting.persistence.service.impl.util.EntityCreator;
import com.akamai.open.moonlighting.persistence.service.impl.util.TimestampUtil;
import static junit.framework.Assert.assertEquals;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class DeleteEntityPropertiesTest extends BaseEntityQueryTestCase {

    public void testQuery() throws Exception {
        System.out.print("testQuery ... ");
        // insert new entity
        final Entity e = EntityCreator.createEntity();
        helper.insert(new InsertEntityQuery(WORKSPACE, e));

        // get the stored entity
        EntityResultSetProcessor processor = new EntityResultSetProcessor();
        helper.select(new SelectEntityById(WORKSPACE, e.getCollection(), e.getId(),
                true), processor);
        final Entity storedEntity = processor.getResult().get(0);

        // insert properties
        helper.insertBatch(new InsertEntityProperty(
                WORKSPACE,
                e.getCollection(),
                e.getId(),
                new TimestampUtil().toTimestamp(storedEntity.getVersion()),
                e.getProperties(),
                e.getProperties().keySet()));

        // delete entity
        int rowsDeleted = helper.delete(new DeleteEntityProperties(WORKSPACE, storedEntity));
        assertEquals(rowsDeleted, e.getProperties().size());
        System.out.println("OK");
    }

}
