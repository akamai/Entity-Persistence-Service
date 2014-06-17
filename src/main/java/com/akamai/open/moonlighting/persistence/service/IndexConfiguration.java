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
package com.akamai.open.moonlighting.persistence.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Define the indexing configuration which ultimately controls searchability of
 * {@link Entity}. Only those properties that are indexed are searchable.
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class IndexConfiguration {

    private final Set<String> columnsToIndex = new HashSet<>();

    public IndexConfiguration(Set<String> columnsToIndex) {
        setColumnsToIndex(columnsToIndex);
    }

    public IndexConfiguration(String... columnsToIndex) {
        if (columnsToIndex != null) {
            setColumnsToIndex(new HashSet<>(Arrays.asList(columnsToIndex)));
        }
    }

    private void setColumnsToIndex(Set<String> columnsToIndex) {
        this.columnsToIndex.clear();
        this.columnsToIndex.addAll(columnsToIndex);
    }

    public Set<String> getColumnsToIndex() {
        return columnsToIndex;
    }

}
