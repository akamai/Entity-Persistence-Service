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

/**
 *
 * @author Shreyas Dube <sdube@akamai.com>
 */
public class MonikerValidator {

    private static final String MONIKER_REGEX = "[a-zA-Z0-9-]+";

    public String validate(final String moniker) {
        if (moniker == null || moniker.trim().isEmpty()) {
            throw new IllegalArgumentException("moniker cannot be null or empty");
        }

        String cleaned = moniker.trim().toLowerCase();
        if (!cleaned.matches(MONIKER_REGEX)) {
            throw new IllegalArgumentException("Invalid moniker: Must be a valid alphanumeric string "
                    + MONIKER_REGEX);
        }

        return cleaned;
    }
}
