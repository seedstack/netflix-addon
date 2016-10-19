/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.feign.internal;

import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.SeedException;
import org.seedstack.seed.core.utils.SeedReflectionUtils;

import javax.inject.Provider;
import java.util.Optional;

class FeignProvider implements Provider<Object> {

    private static final Optional<Class<Object>> HYSTRIX_OPTIONAL = SeedReflectionUtils.optionalOfClass("com.netflix.hystrix.Hystrix");

    @Configuration
    private FeignConfig config;

    private Class<?> feignApi;

    FeignProvider(Class<?> feignApi) {
        this.feignApi = feignApi;
    }

    @Override
    public Object get() {
        FeignConfig.EndpointConfig endpointConfig = config.getEndpoints().get(feignApi);
        Feign.Builder builder;
        // 3 modes :
        //  - no config -> Hystrix builder if in classpath, Feign builder if not
        //  - wrappedWithHystrix == true -> Hystrix builder
        //  - wrappedWithHystrix == false -> Feign builder
        if (endpointConfig.isWrappedWithHystrix() == null) {
            // no config
            if (HYSTRIX_OPTIONAL.isPresent()) {
                builder = HystrixFeign.builder();
            } else {
                builder = Feign.builder();
            }
        } else {
            if (endpointConfig.isWrappedWithHystrix()) {
                builder = HystrixFeign.builder();
            } else {
                builder = Feign.builder();
            }
        }
        Class<Encoder> encoderClass = endpointConfig.getEncoder();
        Class<Decoder> decoderClass = endpointConfig.getDecoder();
        Class<Logger> loggerClass = endpointConfig.getLogger();
        if (encoderClass != null) {
            builder = builder.encoder(instantiateEncoder(encoderClass));
        } else {
            builder = builder.encoder(new JacksonEncoder());
        }
        if (decoderClass != null) {
            builder = builder.decoder(instantiateDecoder(decoderClass));
        } else {
            builder = builder.decoder(new JacksonDecoder());
        }
        if (loggerClass != null) {
            builder = builder.logger(instantiateLogger(loggerClass));
        } else {
            builder = builder.logger(new Slf4jLogger());
        }
        if (endpointConfig.getLogLevel() != null) {
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
        } else {
            builder = builder.logLevel(Logger.Level.NONE);
        }


        return builder.target(feignApi, endpointConfig.getBaseUrl().toExternalForm());
    }

    private Encoder instantiateEncoder(Class<Encoder> encoderClass) {
        try {
            return encoderClass.newInstance();
        } catch (Exception e) {
            throw SeedException.wrap(e, FeignErrorCode.INSTANTIATION_ENCODER_ERROR)
                    .put("class", encoderClass);
        }
    }

    private Decoder instantiateDecoder(Class<Decoder> decoderClass) {
        try {
            return decoderClass.newInstance();
        } catch (Exception e) {
            throw SeedException.wrap(e, FeignErrorCode.INSTANTIATION_DECODER_ERROR)
                    .put("class", decoderClass);
        }
    }

    private Logger instantiateLogger(Class<Logger> loggerClass) {
        try {
            return loggerClass.newInstance();
        } catch (Exception e) {
            throw SeedException.wrap(e, FeignErrorCode.INSTANTIATION_LOGGER_ERROR)
                    .put("class", loggerClass);
        }
    }
}
