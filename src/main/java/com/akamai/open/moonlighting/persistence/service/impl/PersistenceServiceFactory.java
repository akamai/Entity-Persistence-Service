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
import com.akamai.open.moonlighting.rdbms.connection.service.RdbmsConnectionService;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class PersistenceServiceFactory implements ServiceFactory<PersistenceService> {

    private boolean init = false;
    private String failureCause = null;
    // auto set by felixDM
    private RdbmsConnectionService dbConnectionService;

    /**
     * Called by Apache Felix Dependency Manager - init -> start -> stop ->
     * destroy. Initializes the temp directory for JackRabbit's configuration
     * files
     */
    public void init() {
        System.out.println(PersistenceServiceFactory.class.getName() + ".init()");
        init = true;
    }

    /**
     * Called by Apache Felix Dependency Manager - init -> start -> stop ->
     * destroy. Recursively deletes the temp directory for JackRabbit's
     * configuration files
     */
    public void destroy() {
        System.out.println(PersistenceServiceFactory.class.getName() + ".destroy()");
    }

    @Override
    public PersistenceService getService(Bundle bundle, ServiceRegistration<PersistenceService> sr) {
        if (init) {
            // create new instance per bundle
            return new PersistenceServiceImpl(bundle.getBundleContext(),
                    dbConnectionService);
        } else {
            throw new IllegalStateException("PersistenceService was not initialized correctly: "
                    + failureCause);
        }
    }

    @Override
    public void ungetService(Bundle bundle, ServiceRegistration<PersistenceService> sr, PersistenceService s) {
        // TODO
        System.out.println("Not supported yet");
    }

}
