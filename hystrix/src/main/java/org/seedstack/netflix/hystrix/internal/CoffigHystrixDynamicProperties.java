/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import com.netflix.hystrix.strategy.properties.HystrixDynamicProperties;
import com.netflix.hystrix.strategy.properties.HystrixDynamicProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.seedstack.netflix.hystrix.HystrixConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoffigHystrixDynamicProperties implements HystrixDynamicProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoffigHystrixDynamicProperties.class);
    private volatile Map<String, String> properties;

    @Override
    public HystrixDynamicProperty<Integer> getInteger(final String name, final Integer fallback) {
        return new CoffigHystrixDynamicProperty<>(name, Integer::valueOf, fallback);
    }

    @Override
    public HystrixDynamicProperty<String> getString(final String name, final String fallback) {
        return new CoffigHystrixDynamicProperty<>(name, Function.identity(), fallback);
    }

    @Override
    public HystrixDynamicProperty<Long> getLong(final String name, final Long fallback) {
        return new CoffigHystrixDynamicProperty<>(name, Long::valueOf, fallback);
    }

    @Override
    public HystrixDynamicProperty<Boolean> getBoolean(final String name, final Boolean fallback) {
        return new CoffigHystrixDynamicProperty<>(name, Boolean::valueOf, fallback);
    }

    void setHystrixConfig(HystrixConfig hystrixConfig) {
        this.properties = new HashMap<>(hystrixConfig.getProperties());
    }

    private class CoffigHystrixDynamicProperty<T> implements HystrixDynamicProperty<T> {
        private final String name;
        private final Function<String, T> converter;
        private final T fallback;
        private T value;

        CoffigHystrixDynamicProperty(String name, Function<String, T> converter, T fallback) {
            this.name = name;
            this.converter = converter;
            this.fallback = fallback;
            fetchValue();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public void addCallback(Runnable callback) {
            // no support for dynamic properties yet
        }

        private void fetchValue() {
            // fetch the full name
            String valueAsString = properties.get(this.name);
            if (valueAsString == null) {
                // try the name without the hystrix prefix
                valueAsString = properties.get(this.name.substring(8));
            }
            this.value = valueAsString != null ? converter.apply(valueAsString) : fallback;
            LOGGER.trace("Fetched hystrix property {}: {}", this.name, this.value);
        }
    }
}