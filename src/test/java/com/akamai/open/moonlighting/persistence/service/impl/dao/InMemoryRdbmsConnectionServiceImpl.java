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

import com.akamai.open.moonlighting.rdbms.connection.service.RdbmsConnectionService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class InMemoryRdbmsConnectionServiceImpl implements RdbmsConnectionService {

    private static final String CREATE_ENTITIES_TABLE = "CREATE TABLE ENTITIES ( "
            + "WORKSPACE VARCHAR(256), "
            + "COLLECTION VARCHAR(256), "
            + "GUID VARCHAR(64), "
            + "UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "PROPERTIES CLOB, "
            + "DELETED CHAR(1) DEFAULT 'F' CHECK(DELETED IN ('T', 'F')), "
            + "PRIMARY KEY (WORKSPACE, COLLECTION, GUID, UPDATED) "
            + ");";

    private static final String CREATE_ENTITY_PROPERTIES_TABLE = "CREATE TABLE ENTITY_PROPERTIES ( "
            + "WORKSPACE VARCHAR(256), "
            + "COLLECTION VARCHAR(256), "
            + "GUID VARCHAR(64), "
            + "UPDATED TIMESTAMP, "
            + "PROPERTY_NAME VARCHAR(256), "
            + "PROPERTY_VALUE CLOB, "
            + "FOREIGN KEY (WORKSPACE, COLLECTION, GUID, UPDATED) REFERENCES ENTITIES (WORKSPACE, COLLECTION, GUID, UPDATED), "
            + "PRIMARY KEY (WORKSPACE, COLLECTION, GUID, PROPERTY_NAME) "
            + ");";

    private final String dbName;

    public InMemoryRdbmsConnectionServiceImpl(String dbName) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        this.dbName = dbName;
        createTable(CREATE_ENTITIES_TABLE);
        createTable(CREATE_ENTITY_PROPERTIES_TABLE);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return createConnection();
    }

    private void createTable(String query) throws SQLException {
        Connection c = null;
        try {
            c = createConnection();

            Statement st = null;
            try {
                st = c.createStatement();

                System.out.println("Executing " + this + ": " + query);
                int i = st.executeUpdate(query);    // run the query

                if (i == -1) {
                    System.err.println("[ERROR] Could not execute query: " + query);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw ex;
            } finally {
                if (st != null && !st.isClosed()) {
                    st.close();
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:" + dbName + ";shutdown=false;");
    }

}
