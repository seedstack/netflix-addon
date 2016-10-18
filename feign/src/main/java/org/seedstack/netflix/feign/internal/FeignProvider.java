/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.feign.internal;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
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
        if (encoderClass != null) {
            builder = builder.encoder(instantiateEncoder(encoderClass));
        }
        if (decoderClass != null) {
            builder = builder.decoder(instantiateDecoder(decoderClass));
        }
        return builder.target(feignApi, endpointConfig.getBaseUrl().toExternalForm());
    }

    private Encoder instantiateEncoder(Class<Encoder> encoderClass) {
        try {
            return encoderClass.newInstance();
        } catch (InstantiationException e) {
            // TODO : the class endpointConfig.getEncoder() cannot be instantiated
            // possible causes : the class is an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO : the class endpointConfig.getEncoder() or its nullary constructor is not accessible
            e.printStackTrace();
        }
        return null;
    }

    private Decoder instantiateDecoder(Class<Decoder> decoderClass) {
        try {
            return decoderClass.newInstance();
        } catch (InstantiationException e) {
            // TODO : the class endpointConfig.getDecoder() cannot be instantiated
            // possible causes : the class is an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO : the class endpointConfig.getDecoder() or its nullary constructor is not accessible
            e.printStackTrace();
        }
        return null;
    }
}
