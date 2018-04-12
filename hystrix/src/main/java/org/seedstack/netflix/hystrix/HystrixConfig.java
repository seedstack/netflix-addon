/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
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
import org.seedstack.coffig.SingleValue;

@Config("netflix.hystrix")
public class HystrixConfig {
    private MetricsConfig metrics = new MetricsConfig();
    private Map<String, String> properties = new HashMap<>();

    public MetricsConfig metrics() {
        return metrics;
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public HystrixConfig setProperties(Map<String, String> properties) {
        this.properties = new HashMap<>(properties);
        return this;
    }

    public HystrixConfig addProperty(String name, String value) {
        this.properties.put(name, value);
        return this;
    }

    @Config("metrics")
    public static class MetricsConfig {
        @SingleValue
        private boolean enabled = true;
        private String servletPath = "/hystrix.stream";

        public boolean isEnabled() {
            return enabled;
        }

        public MetricsConfig setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public String getServletPath() {
            return servletPath;
        }

        public MetricsConfig setServletPath(String servletPath) {
            this.servletPath = servletPath;
            return this;
        }
    }
}
