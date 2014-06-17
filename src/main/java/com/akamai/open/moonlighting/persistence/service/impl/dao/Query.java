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
package com.akamai.open.moonlighting.persistence.service.impl.dao;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public enum Query {

    DELETE_ENTITY_BY_ID("UPDATE ENTITIES SET DELETED = 'T' WHERE workspace = ? AND collection = ? AND guid = ?"),
    DELETE_ENTITY_PROPERTIES("DELETE FROM ENTITY_PROPERTIES WHERE workspace = ? AND collection = ? AND guid = ?"),
    // --
    INSERT_ENTITY("INSERT INTO ENTITIES (workspace, collection, guid, properties) VALUES (?, ?, ?, ?)"),
    INSERT_ENTITY_PROPERTIES("INSERT INTO ENTITY_PROPERTIES (workspace, collection, guid, updated, property_name, property_value) VALUES (?, ?, ?, ?, ?, ?)"),
    // --
    SELECT_ALL_ENTITIES("SELECT * FROM ENTITIES WHERE DELETED = 'F' ORDER BY updated ASC"),
    SELECT_ENTITY_BY_ID("SELECT * FROM (SELECT * FROM ENTITIES WHERE workspace = ? AND collection = ? AND guid = ? AND DELETED = 'F' ORDER BY UPDATED DESC) WHERE ROWNUM = 1",
            "SELECT * FROM ENTITIES WHERE workspace = ? AND collection = ? AND guid = ? AND DELETED = 'F' ORDER BY UPDATED DESC LIMIT 1"),
    SELECT_ENTITIES_BY_TYPE("SELECT * FROM ENTITIES e, "
            + "(SELECT DISTINCT workspace, collection, guid, updated FROM ENTITY_PROPERTIES WHERE workspace = ? AND collection = ?) ep "
            + "WHERE e.workspace = ep.workspace AND e.collection = ep.collection AND e.guid = ep.guid AND e.updated = ep.updated AND e.deleted = 'F' "
            + "ORDER BY e.updated DESC"),
    SELECT_ENTITIES_BY_REGEX("SELECT * FROM ENTITIES e, "
            + "(SELECT DISTINCT self_uri, updated FROM ENTITY_PROPERTIES WHERE REGEXP_LIKE(self_uri, ?, 'i')) p "
            + "WHERE e.self_uri = p.self_uri AND e.updated = p.updated AND e.DELETED = 'F' "
            + "ORDER BY e.updated DESC",
            "SELECT * FROM ENTITIES e, "
            + "(SELECT DISTINCT self_uri, updated FROM ENTITY_PROPERTIES WHERE REGEXP_MATCHES(self_uri, ?)) p "
            + "WHERE e.self_uri = p.self_uri AND e.updated = p.updated "
            + "ORDER BY e.updated DESC");

    private final String sql;
    private final String junitSql;

    private Query(String sql) {
        this(sql, sql);
    }

    private Query(String sql, String junitSql) {
        this.sql = sql;
        this.junitSql = junitSql;
    }

    public String getSql() {
        return sql;
    }

    public String getJunitSql() {
        return junitSql;
    }
}
