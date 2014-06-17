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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class SelectEntitiesByUriRegex extends EntityQuery {

    private final String selfUriRegex;
    private final boolean unitTest;

    public SelectEntitiesByUriRegex(String selfUriRegex) {
        this(selfUriRegex, false);
    }

    public SelectEntitiesByUriRegex(String selfUriRegex, boolean unitTest) {
        this.selfUriRegex = selfUriRegex;
        this.unitTest = unitTest;
    }

    @Override
    public String getQuery() {
        final Query q = Query.SELECT_ENTITIES_BY_REGEX;
        if (unitTest) {
            return q.getJunitSql();
        } else {
            return q.getSql();
        }
    }

    @Override
    public PreparedStatementInitializer getPreparedStatementInitializer() {
        return new PreparedStatementInitializer() {

            @Override
            public void initialize(Connection c, PreparedStatement s) throws SQLException {
                s.setString(1, selfUriRegex);
            }
        };
    }

}
