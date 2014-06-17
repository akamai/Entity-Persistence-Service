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

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class TimestampUtil {

    public Long toLong(final Timestamp timestamp) {
        return ((timestamp.getTime() / 1000) * 1000 * 1000 * 1000) + timestamp.getNanos();
    }

    public Timestamp toTimestamp(final Long time) {
        final long millis = time / (1000 * 1000);
        final int nanos = (int) (time % (1000 * 1000 * 1000));

        final Timestamp ts = new Timestamp(millis);
        ts.setNanos(nanos);

        return ts;
    }
}
