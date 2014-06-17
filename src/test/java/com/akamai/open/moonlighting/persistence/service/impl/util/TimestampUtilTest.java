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

import java.sql.Timestamp;
import junit.framework.TestCase;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class TimestampUtilTest extends TestCase {

    public void testToLong() {
        System.out.print("testToLong ... ");
        final long now = 1398355766000L;
        final int nanos = 123456000;
        final Timestamp ts = new Timestamp(now);
        ts.setNanos(nanos);

        final Long result = new TimestampUtil().toLong(ts);
        assertNotNull(result);
        assertEquals(result, Long.valueOf("1398355766123456000"));
        System.out.println("OK");
    }

    public void testToTimestamp() {
        System.out.print("testToTimestamp ... ");

        final Timestamp ts = new TimestampUtil().toTimestamp(1398355766123456000L);
        assertNotNull(ts);
        assertEquals(ts.getTime(), 1398355766123L);
        assertEquals(ts.getNanos(), 123456000);

        System.out.println("OK");
    }

}
