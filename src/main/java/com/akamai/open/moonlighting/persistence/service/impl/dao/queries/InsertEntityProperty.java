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

import com.akamai.open.moonlighting.persistence.service.impl.dao.EntityQuery;
import com.akamai.open.moonlighting.persistence.service.impl.dao.Query;
import com.akamai.open.moonlighting.persistence.service.impl.util.ClobUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class InsertEntityProperty extends EntityQuery {

    private final String workspace;
    private final String type;
    private final String id;
    private final Timestamp updated;
    private final Map<String, String> properties;
    private final ClobUtil clobUtil;
    private final Set<String> columnsToIndex;

    public InsertEntityProperty(String workspace, String type, String id,
            Timestamp updated, Map<String, String> properties, Set<String> columnsToIndex) {
        this.workspace = workspace;
        this.type = type;
        this.id = id;
        this.updated = updated;
        this.properties = properties;
        this.columnsToIndex = columnsToIndex;
        this.clobUtil = new ClobUtil();
    }

    @Override
    public String getQuery() {
        return Query.INSERT_ENTITY_PROPERTIES.getSql();
    }

    @Override
    public PreparedStatementInitializer getPreparedStatementInitializer() {
        return new PreparedStatementInitializer() {

            @Override
            public void initialize(Connection c, PreparedStatement s) throws SQLException {
                // for each property, insert in the entity properties table
                for (Map.Entry<String, String> property : properties.entrySet()) {
                    final String propertyName = property.getKey();
                    if (columnsToIndex.contains(propertyName)) {
                        final String propertyValue = property.getValue();
                        System.out.println("Indexing entityProperty: " + propertyName
                                + " ==> " + propertyValue);
                        s.setString(1, workspace);
                        s.setString(2, type);
                        s.setString(3, id);
                        s.setTimestamp(4, updated);
                        s.setString(5, propertyName);
                        clobUtil.setClob(s, 6, propertyValue);
                        s.addBatch();
                    } else {
                        System.out.println("Not Indexing entityProperty: " + propertyName);
                    }
                }

            }
        };
    }

}
