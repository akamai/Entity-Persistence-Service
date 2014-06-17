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

import com.akamai.open.moonlighting.persistence.service.impl.dao.EntityDao;
import com.akamai.open.moonlighting.persistence.service.impl.dao.InMemoryRdbmsConnectionServiceImpl;
import com.akamai.open.moonlighting.persistence.service.impl.dao.SqlHelper;
import com.akamai.open.moonlighting.persistence.service.impl.util.TestConstants;
import com.akamai.open.moonlighting.persistence.service.impl.util.UuidGenerator;
import com.akamai.open.moonlighting.rdbms.connection.service.RdbmsConnectionService;
import junit.framework.TestCase;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class BaseEntityQueryTestCase extends TestCase {

    protected static final String WORKSPACE = TestConstants.WORKSPACE;
    // --
    protected EntityDao dao;
    protected SqlHelper helper;

    @Override
    protected void setUp() throws Exception {
        final RdbmsConnectionService dbConnectionService = new InMemoryRdbmsConnectionServiceImpl(UuidGenerator.generateUuid());
        dao = new EntityDao(WORKSPACE, dbConnectionService, true);
        helper = new SqlHelper(dbConnectionService.getConnection());
    }

    @Override
    protected void tearDown() throws Exception {
        dao.close();
        helper.close();
    }

    public void testHelperCreated() throws Exception {
        System.out.print("testHelperCreated ... ");
        assertNotNull(helper);
        System.out.println("OK");
    }
}
