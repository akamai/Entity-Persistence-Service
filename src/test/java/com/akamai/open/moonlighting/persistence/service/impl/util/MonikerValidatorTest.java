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

import junit.framework.TestCase;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class MonikerValidatorTest extends TestCase {

    private MonikerValidator validator = new MonikerValidator();

    public void testValidateUpperCase() {
        System.out.print("testValidateUpperCase ... ");
        final String s = "FOO-BAR";
        assertEquals(s.toLowerCase(), validator.validate(s));
        System.out.println("OK");
    }

    public void testValidateWithSpaces() {
        System.out.print("testValidateWithSpaces ... ");
        final String s = " Foo-bAR  ";
        assertEquals(s.trim().toLowerCase(), validator.validate(s));
        System.out.println("OK");
    }

    public void testValidateNull() {
        System.out.print("testValidateNull ... ");
        try {
            validator.validate(null);
            fail();
        } catch (IllegalArgumentException ex) {
            System.out.println("OK");
        }
    }

    public void testValidateEmpty() {
        System.out.print("testValidateEmpty ... ");
        try {
            validator.validate(" ");
            fail();
        } catch (IllegalArgumentException ex) {
            System.out.println("OK");
        }
    }

    public void testValidateInvalidMoniker() {
        System.out.print("testValidateInvalidMoniker ... ");
        try {
            validator.validate(" Foo_bAR  ");
            fail();
        } catch (IllegalArgumentException ex) {
            System.out.println("OK");
        }
    }

}
