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
public class UriBuilderTest extends TestCase {

    private static final String UUID = UuidGenerator.generateUuid();
    private UriBuilder uriBuilder;

    public UriBuilderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        uriBuilder = new UriBuilder("foo");
    }

    @Override
    protected void tearDown() throws Exception {
        uriBuilder = null;
    }

    public void testToUriNullType() {
        System.out.print("testToUriNullType ... ");
        try {
            uriBuilder.toUri(null);
            fail("Should not specify null type");
        } catch (IllegalArgumentException ex) {
            System.out.println("OK");
        }
    }

    public void testToUriEmptyType() {
        System.out.print("testToUriEmptyType ... ");
        try {
            uriBuilder.toUri("  ");
            fail("Should not specify empty type");
        } catch (IllegalArgumentException ex) {
            System.out.println("OK");
        }
    }

    public void testToUriLeadingSlash() {
        System.out.print("testToUriLeadingSlash ... ");
        final String type = "/bar/baz";
        final String uri = uriBuilder.toUri(type, UUID);
        assertEquals(uri, toUri(type));
        System.out.println("OK");
    }

    public void testToUriTrailingSlash() {
        System.out.print("testToUriTrailingSlash ... ");
        final String type = "bar/baz/";
        final String uri = uriBuilder.toUri(type, UUID);
        assertEquals(uri, toUri("/" + type.substring(0, type.length() - 1)));
        System.out.println("OK");
    }

    public void testToUriLeadingAndTrailingSlash() {
        System.out.print("testToUriLeadingAndTrailingSlash ... ");
        final String type = "/bar/baz/";
        final String uri = uriBuilder.toUri(type, UUID);
        assertEquals(uri, toUri(type.substring(0, type.length() - 1)));
        System.out.println("OK");
    }

    public void testToUriContainsDashes() {
        System.out.print("testToUriContainsDashes ... ");
        final String type = "a-b-c/d-e-f";
        final String uri = uriBuilder.toUri(type, UUID);
        assertEquals(uri, toUri("/" + type));
        System.out.println("OK");
    }
    
    public void testToUriDeepNesting() {
        System.out.print("testToUriDeepNesting ... ");
        final String type = "a/b/c/d/e/f/g/h/i/j";
        final String uri = uriBuilder.toUri(type, UUID);
        assertEquals(uri, toUri("/" + type));
        System.out.println("OK");
    }

    public void testToUriInvalidUri() {
        System.out.print("testToUriInvalidUri ... ");
        final String type = "a_b_c";
        try {
            uriBuilder.toUri(type, UUID);
            fail("Invalid URI");
        } catch (IllegalArgumentException ex) {
            System.out.println("OK");
        }
    }

    private String toUri(final String type) {
        return "/" + uriBuilder.getWorkspaceName() + type + "/" + UUID;
    }

}
