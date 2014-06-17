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

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class ClobUtil {

    public String toString(final Clob clob) throws SQLException, IOException {
        final Reader r = clob.getCharacterStream();
        final StringWriter w = new StringWriter();

        int c;
        while ((c = r.read()) != -1) {
            w.write(c);
        }
        w.flush();
        return w.toString();
    }

    /**
     * Doesn't perform as well as I thought it would.
     *
     * @param c
     * @param s
     * @return
     * @throws SQLException
     * @deprecated use {@link setClob()} instead
     */
    @Deprecated
    public Clob toClob(final Connection c, final String s) throws SQLException {
        final Clob clob = c.createClob();
        clob.setString(1, s);
        return clob;
    }

    /**
     * Sets clob using http://stackoverflow.com/a/18942024/574776 approach
     *
     * @param st
     * @param i
     * @param s
     * @throws SQLException
     */
    public void setClob(final PreparedStatement st, final int i, final String s) throws SQLException {
        st.setString(i, s);
    }
}
