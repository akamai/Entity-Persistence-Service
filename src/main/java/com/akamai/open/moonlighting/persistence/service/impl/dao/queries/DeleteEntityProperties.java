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
import com.akamai.open.moonlighting.persistence.service.impl.dao.EntityQuery;
import com.akamai.open.moonlighting.persistence.service.impl.dao.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class DeleteEntityProperties extends EntityQuery {

    private final String workspace;
    private final String collection;
    private final String id;

    public DeleteEntityProperties(final String workspace, final Entity e) {
        if (e == null) {
            throw new IllegalArgumentException("entity cannot be null or empty");
        }
        this.workspace = workspace;
        this.collection = e.getCollection();
        this.id = e.getId();
    }

    @Override
    public String getQuery() {
        return Query.DELETE_ENTITY_PROPERTIES.getSql();
    }

    @Override
    public PreparedStatementInitializer getPreparedStatementInitializer() {
        return new PreparedStatementInitializer() {

            @Override
            public void initialize(Connection c, PreparedStatement s) throws SQLException {
                s.setString(1, workspace);
                s.setString(2, collection);
                s.setString(3, id);
            }
        };
    }

}
