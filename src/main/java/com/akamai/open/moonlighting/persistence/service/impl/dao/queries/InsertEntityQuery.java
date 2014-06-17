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
import com.akamai.open.moonlighting.persistence.service.impl.util.ClobUtil;
import com.akamai.open.moonlighting.persistence.service.impl.util.JsonUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class InsertEntityQuery extends EntityQuery {

    private final String workspace;
    private final String collection;
    private final String id;
    private final String propertiesJson;
    private final ClobUtil clobUtil = new ClobUtil();
    private final JsonUtil jsonUtil = new JsonUtil();

    public InsertEntityQuery(String workspace, Entity e) throws IOException {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        this.workspace = workspace;
        this.collection = e.getCollection();
        this.id = e.getId();
        propertiesJson = jsonUtil.toJson(e.getProperties());
    }

    @Override
    public String getQuery() {
        return Query.INSERT_ENTITY.getSql();
    }

    @Override
    public PreparedStatementInitializer getPreparedStatementInitializer() {
        return new PreparedStatementInitializer() {

            @Override
            public void initialize(Connection c, PreparedStatement s) throws SQLException {
                // set the workspace
                s.setString(1, workspace);

                // set the type
                s.setString(2, collection);

                // set the id
                s.setString(3, id);

                // set properties
                clobUtil.setClob(s, 4, propertiesJson);
            }
        };
    }

}
