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
package com.akamai.open.moonlighting.persistence.service.impl;

import com.akamai.open.moonlighting.persistence.service.PersistenceService;
import com.akamai.open.moonlighting.persistence.service.Workspace;
import com.akamai.open.moonlighting.rdbms.connection.service.RdbmsConnectionService;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class PersistenceServiceImpl implements PersistenceService {

    private static final Logger LOG = Logger.getLogger(PersistenceServiceImpl.class);
    private final BundleContext bundleContext;
    private final RdbmsConnectionService dbConnectionService;

    public PersistenceServiceImpl(BundleContext bundleContext,
            RdbmsConnectionService dbConnectionService) {
        this.bundleContext = bundleContext;
        this.dbConnectionService = dbConnectionService;

    }

    @Override
    public Workspace getWorkspace(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Workspace name cannot be null or empty");
        }

        // remove leading '/'
        if (name.startsWith("/")) {
            name = name.substring(1);
        }

        // remove trailing '/'
        if (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }

        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Workspace name cannot be empty");
        }

        return new WorkspaceImpl(name, dbConnectionService);
    }

    @Override
    public Workspace getDefaultWorkspace() {
        return getWorkspace("-");
    }

}
