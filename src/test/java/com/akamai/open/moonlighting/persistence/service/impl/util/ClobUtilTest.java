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
package com.akamai.open.moonlighting.persistence.service.impl.util;

import com.akamai.open.moonlighting.persistence.service.impl.dao.InMemoryRdbmsConnectionServiceImpl;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.Connection;
import junit.framework.TestCase;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class ClobUtilTest extends TestCase {

    private Connection connection;
    private final String smallString = "hello, world!";
    private String largeString;

    @Override
    protected void setUp() throws Exception {
        connection = new InMemoryRdbmsConnectionServiceImpl(UuidGenerator.generateUuid()).getConnection();
        // 1MB
        final int size = 1000 * 1000;
        final String s = "hello, world!";

        final StringWriter w = new StringWriter();
        for (int i = 0; i < size; i++) {
            w.write(s);
        }
        largeString = w.toString();
    }

    @Override
    protected void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void testClobToString() throws Exception {
        System.out.print("testClobToString ... ");

        final Clob c = connection.createClob();
        c.setString(1, smallString);

        final String result = new ClobUtil().toString(c);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result, smallString);
        System.out.println("OK");
    }

    public void testLargeClobToString() throws Exception {
        System.out.print("testLargeClobToString ... ");

        final Clob c = connection.createClob();
        c.setString(1, largeString);

        final String result = new ClobUtil().toString(c);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result, largeString);
        System.out.println("OK");
    }

    public void testStringToClob() throws Exception {
        System.out.print("testStringToClob ... ");
        final Clob c = new ClobUtil().toClob(connection, smallString);
        assertNotNull(c);
        assertEquals(new ClobUtil().toString(c), smallString);
        System.out.println("OK");
    }

    public void testLargeStringToClob() throws Exception {
        System.out.print("testLargeStringToClob ... ");
        final Clob c = new ClobUtil().toClob(connection, largeString);
        assertNotNull(c);
        assertEquals(new ClobUtil().toString(c), largeString);
        System.out.println("OK");
    }
}
