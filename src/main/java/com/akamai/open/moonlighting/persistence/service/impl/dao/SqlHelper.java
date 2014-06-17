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

import com.akamai.open.moonlighting.persistence.service.impl.dao.rs.ResultSetProcessor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class SqlHelper {

    private static final Logger LOG = Logger.getLogger(SqlHelper.class);
    private final Connection connection;

    private static interface Callback<R> {

        R execute(PreparedStatement s) throws SQLException;
    }

    public SqlHelper(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    public Integer insert(EntityQuery query) throws SQLException {
        return executeInsertUpdateDelete(query);
    }

    public Integer insertBatch(EntityQuery query) throws SQLException {
        return executeInsertBatch(query);
    }

    public Integer update(EntityQuery query) throws SQLException {
        return executeInsertUpdateDelete(query);
    }

    public Integer delete(EntityQuery query) throws SQLException {
        return executeInsertUpdateDelete(query);
    }

    public void select(EntityQuery query, ResultSetProcessor rsProcessor) throws SQLException {
        executeSelect(query, rsProcessor);
    }

    private Integer executeInsertUpdateDelete(EntityQuery query) throws SQLException {
        return executeQuery(query, new Callback<Integer>() {

            @Override
            public Integer execute(PreparedStatement s) throws SQLException {
                return s.executeUpdate();
            }
        });
    }

    private Integer executeInsertBatch(EntityQuery query) throws SQLException {
        return executeQuery(query, new Callback<Integer>() {

            @Override
            public Integer execute(PreparedStatement s) throws SQLException {
                return s.executeBatch().length;
            }
        });
    }

    private void executeSelect(EntityQuery query, final ResultSetProcessor rsProcessor) throws SQLException {
        executeQuery(query, new Callback<Void>() {

            @Override
            public Void execute(PreparedStatement s) throws SQLException {
                rsProcessor.process(s.executeQuery());
                return null;
            }
        });
    }

    private <T> T executeQuery(EntityQuery query,
            Callback<T> callback) throws SQLException {

        PreparedStatement s = null;
        try {
            LOG.debug("Preparing statement");
            s = connection.prepareStatement(query.getQuery());
            if (query.getPreparedStatementInitializer() != null) {
                // do custom initialization here
                query.getPreparedStatementInitializer().initialize(connection, s);
            }

            try {
                LOG.debug("Executing ... " + query.getQuery());
                return callback.execute(s);
            } catch (SQLException ex) {
                // abort! 
                LOG.warn("Rollback!", ex);
                connection.rollback();
                throw ex;
            }
        } finally {
            if (s != null && !s.isClosed()) {
                LOG.debug("Closing statement");
                s.close();
            }
        }
    }

    public void commit() throws SQLException {
        LOG.info("Commit!");
        connection.commit();
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            LOG.debug("Closing connection");
            connection.close();
        }
    }

}
