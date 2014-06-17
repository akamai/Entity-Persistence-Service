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

import org.apache.log4j.Logger;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class UriBuilder {

    private static final Logger LOG = Logger.getLogger(UriBuilder.class);
    private static final String ALPHANUM_REGEX = "[a-zA-Z0-9-]+";
    private static final String MONIKER_REGEX = ALPHANUM_REGEX + "[/" + ALPHANUM_REGEX + "]*";
    private final String workspaceName;

    public UriBuilder(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String toUri(String resourceMoniker) {
        return toUri(resourceMoniker, UuidGenerator.generateUuid());
    }

    public String toUri(String resourceMoniker, final String uuid) {
        resourceMoniker = validatedMoniker(resourceMoniker);

        final StringBuilder b = new StringBuilder("/")
                .append(workspaceName)
                .append("/")
                .append(resourceMoniker)
                .append("/")
                .append(uuid);
        LOG.info("constructed Uri: " + b.toString());
        return b.toString();
    }

    private String validatedMoniker(String resourceMoniker) {
        if (resourceMoniker == null || resourceMoniker.trim().isEmpty()) {
            throw new IllegalArgumentException("resourceMoniker cannot be null or empty");
        }

        // remove leading '/'
        if (resourceMoniker.startsWith("/")) {
            resourceMoniker = resourceMoniker.substring(1);
        }

        // remove trailing '/'
        if (resourceMoniker.endsWith("/")) {
            resourceMoniker = resourceMoniker.substring(0, resourceMoniker.length() - 1);
        }

        if (resourceMoniker.trim().isEmpty()) {
            throw new IllegalArgumentException("resourceMoniker cannot be empty or just '/'");
        }

        if (!resourceMoniker.matches(MONIKER_REGEX)) {
            throw new IllegalArgumentException("Invalid resourceMoniker: Must be a valid alphanumeric string "
                    + MONIKER_REGEX);
        }

        return resourceMoniker;
    }

    protected String getWorkspaceName() {
        return workspaceName;
    }

}
