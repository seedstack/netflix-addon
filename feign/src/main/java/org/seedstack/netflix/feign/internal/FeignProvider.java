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
import feign.hystrix.HystrixFeign;
import feign.slf4j.Slf4jLogger;
import org.seedstack.seed.Configuration;

import javax.inject.Provider;

public class FeignProvider implements Provider<Object> {

    @Configuration
    private FeignConfig config;

    private Class<?> feignApi;

    FeignProvider(Class<?> feignApi) {
        this.feignApi = feignApi;
    }

    @Override
    public Object get() {
        FeignConfig.EndpointConfig endpointConfig = config.getEndpoints().get(feignApi);
        HystrixFeign.Builder builder = HystrixFeign.builder();
        Class<Encoder> encoderClass = endpointConfig.getEncoder();
        Class<Decoder> decoderClass = endpointConfig.getDecoder();
        Class<Logger> loggerClass = endpointConfig.getLogger();
        if (encoderClass != null) {
            builder = builder.encoder(instantiateEncoder(encoderClass));
        }
        if (decoderClass != null) {
            builder = builder.decoder(instantiateDecoder(decoderClass));
        }
        if (loggerClass != null) {
            builder = builder.logger(instantiateLogger(loggerClass));
        } else {
            builder = builder.logger(new Slf4jLogger());
        }
        switch (endpointConfig.getLogLevel()) {
            case "NONE":
                builder = builder.logLevel(Logger.Level.NONE);
                break;
            case "BASIC":
                builder = builder.logLevel(Logger.Level.BASIC);
                break;
            case "HEADERS":
                builder = builder.logLevel(Logger.Level.HEADERS);
                break;
            case "FULL":
                builder = builder.logLevel(Logger.Level.FULL);
                break;
            default:
                builder = builder.logLevel(Logger.Level.NONE);
                break;
        }


        return builder.target(feignApi, endpointConfig.getBaseUrl().toExternalForm());
    }

    private Encoder instantiateEncoder(Class<Encoder> encoderClass) {
        try {
            return encoderClass.newInstance();
        } catch (InstantiationException e) {
            // TODO : the class encoderClass cannot be instantiated
            // possible causes : the class is an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO : the class encoderClass or its nullary constructor is not accessible
            e.printStackTrace();
        }
        return null;
    }

    private Decoder instantiateDecoder(Class<Decoder> decoderClass) {
        try {
            return decoderClass.newInstance();
        } catch (InstantiationException e) {
            // TODO : the class decoderClass cannot be instantiated
            // possible causes : the class is an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO : the class decoderClass or its nullary constructor is not accessible
            e.printStackTrace();
        }
        return null;
    }

    private Logger instantiateLogger(Class<Logger> loggerClass) {
        try {
            return loggerClass.newInstance();
        } catch (InstantiationException e) {
            // TODO : the class loggerClass cannot be instantiated
            // possible causes : the class is an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO : the class loggerClass or its nullary constructor is not accessible
            e.printStackTrace();
        }
        return null;
    }
}
