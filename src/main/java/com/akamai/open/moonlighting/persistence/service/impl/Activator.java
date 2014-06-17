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
import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class Activator extends DependencyActivatorBase {

    private static final Logger LOG = Logger.getLogger(Activator.class);
    private Component persistenceComponent;

    @Override
    public void destroy(final BundleContext context, final DependencyManager manager)
            throws Exception {
        log("Deactivating " + context.getBundle().getSymbolicName());
        if (persistenceComponent != null) {
            manager.remove(persistenceComponent);
        }
    }

    @Override
    public void init(final BundleContext context, final DependencyManager manager)
            throws Exception {
        log("Activating " + context.getBundle().getSymbolicName());

        persistenceComponent = createComponent()
                .setInterface(PersistenceService.class.getName(), null)
                .setImplementation(PersistenceServiceFactory.class)
                .add(createServiceDependency()
                        .setService(RdbmsConnectionService.class)
                        .setRequired(true));
        manager.add(persistenceComponent);
    }

    private void log(final String msg) {
        System.out.println(msg);
        LOG.info(msg);
    }
}
