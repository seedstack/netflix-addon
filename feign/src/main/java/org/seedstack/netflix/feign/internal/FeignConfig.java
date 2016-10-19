/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.feign.internal;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.seedstack.coffig.Config;
import org.seedstack.coffig.SingleValue;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Config("feign")
public class FeignConfig {
    private Map<Class<?>, EndpointConfig> endpoints = new HashMap<>();

    Map<Class<?>, EndpointConfig> getEndpoints() {
        return Collections.unmodifiableMap(endpoints);
    }

    public void addEndpoint(Class<?> endpointClass, EndpointConfig endpoint) {
        endpoints.put(endpointClass, endpoint);
    }

    public static class EndpointConfig {
        @SingleValue
        @NotNull
        private URL baseUrl;

        private Class<Encoder> encoder;

        private Class<Decoder> decoder;

        private Class<Logger> logger;

        /**
         * NONE, BASIC, HEADERS, FULL
         *
         * @see Logger.Level
         */
        private String logLevel;

        private Boolean wrappedWithHystrix;

        public URL getBaseUrl() {
            return baseUrl;
        }

        public EndpointConfig setBaseUrl(URL baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Class<Encoder> getEncoder() {
            return encoder;
        }

        public EndpointConfig setEncoder(Class<Encoder> encoder) {
            this.encoder = encoder;
            return this;
        }

        public Class<Decoder> getDecoder() {
            return decoder;
        }

        public EndpointConfig setDecoder(Class<Decoder> decoder) {
            this.decoder = decoder;
            return this;
        }

        public Class<Logger> getLogger() {
            return logger;
        }

        public EndpointConfig setLogger(Class<Logger> logger) {
            this.logger = logger;
            return this;
        }

        public String getLogLevel() {
            return logLevel;
        }

        public EndpointConfig setLogLevel(String logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Boolean isWrappedWithHystrix() {
            return wrappedWithHystrix;
        }

        public EndpointConfig setWrappedWithHystrix(Boolean wrappedWithHystrix) {
            this.wrappedWithHystrix = wrappedWithHystrix;
            return this;
        }
    }
}
