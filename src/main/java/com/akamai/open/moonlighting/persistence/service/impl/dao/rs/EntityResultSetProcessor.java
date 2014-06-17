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
package com.akamai.open.moonlighting.persistence.service.impl.dao.rs;

import com.akamai.open.moonlighting.persistence.service.Entity;
import com.akamai.open.moonlighting.persistence.service.exception.InvalidEntityException;
import com.akamai.open.moonlighting.persistence.service.impl.EntityImpl;
import com.akamai.open.moonlighting.persistence.service.impl.util.ClobUtil;
import com.akamai.open.moonlighting.persistence.service.impl.util.JsonUtil;
import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Creates <code>Entity</code> objects from a <code>ResultSet</code>
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class EntityResultSetProcessor implements ResultSetProcessor<List<Entity>> {

    private static final Logger LOG = Logger.getLogger(EntityResultSetProcessor.class);
    private static final JsonUtil jsonUtil = new JsonUtil();
    private List<Entity> entities = new ArrayList<>();

    @Override
    public void process(ResultSet rs) throws SQLException {
        while (rs.next()) {
            entities.add(toEntity(rs));
        }
    }

    @Override
    public List<Entity> getResult() {
        return entities;
    }

    private Entity toEntity(ResultSet rs) throws SQLException {
        final String collection = rs.getString("collection");
        final String id = rs.getString("guid");
        final Timestamp updated = rs.getTimestamp("updated");
        final Clob clob = rs.getClob("properties");

        try {
            final String properties = new ClobUtil().toString(clob);
            return new EntityImpl(collection,
                    id,
                    jsonUtil.toMap(properties),
                    updated);
        } catch (IOException ex) {
            LOG.error(ex);
            throw new InvalidEntityException(collection + ":" + id);
        }
    }

}
