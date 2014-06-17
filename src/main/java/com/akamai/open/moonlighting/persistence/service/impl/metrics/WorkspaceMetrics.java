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
package com.akamai.open.moonlighting.persistence.service.impl.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class WorkspaceMetrics {

    private static final String CREATE_METRIC = MetricRegistry.name(WorkspaceMetrics.class, "create");
    private static final String READ_METRIC = MetricRegistry.name(WorkspaceMetrics.class, "read");
    private static final String UPDATE_METRIC = MetricRegistry.name(WorkspaceMetrics.class, "update");
    private static final String DELETE_METRIC = MetricRegistry.name(WorkspaceMetrics.class, "delete");
    private static final String FIND_METRIC = MetricRegistry.name(WorkspaceMetrics.class, "find");

    private final Timer createTimer;
    private final Timer readTimer;
    private final Timer updateTimer;
    private final Timer deleteTimer;
    private final Timer findTimer;

    public WorkspaceMetrics(final MetricRegistry metricRegistry) {
        createTimer = metricRegistry.timer(CREATE_METRIC);
        readTimer = metricRegistry.timer(READ_METRIC);
        updateTimer = metricRegistry.timer(UPDATE_METRIC);
        deleteTimer = metricRegistry.timer(DELETE_METRIC);
        findTimer = metricRegistry.timer(FIND_METRIC);
    }

//    @Override
//    public Map<String, Metric> getMetrics() {
//        Map<String, Metric> metrics = new HashMap<>();
//        metrics.put(CREATE_METRIC, createTimer);
//        metrics.put(READ_METRIC, readTimer);
//        metrics.put(UPDATE_METRIC, updateTimer);
//        metrics.put(DELETE_METRIC, deleteTimer);
//        metrics.put(FIND_METRIC, findTimer);
//        return metrics;
//    }
    public Timer getCreateTimer() {
        return createTimer;
    }

    public Timer getReadTimer() {
        return readTimer;
    }

    public Timer getUpdateTimer() {
        return updateTimer;
    }

    public Timer getDeleteTimer() {
        return deleteTimer;
    }

    public Timer getFindTimer() {
        return findTimer;
    }

}
