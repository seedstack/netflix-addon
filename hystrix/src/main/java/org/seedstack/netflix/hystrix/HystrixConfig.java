/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.seedstack.coffig.Config;

@Config("netflix.hystrix")
public class HystrixConfig {
    private Map<String, String> properties = new HashMap<>();

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = new HashMap<>(properties);
    }

    public void addProperty(String name, String value) {
        this.properties.put(name, value);
    }
}
