/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class MethodUtils {

    public static Method getFallbackMethod(Class<?> type, Method commandMethod) {
        if (commandMethod.isAnnotationPresent(HystrixCommand.class)) {
            HystrixCommand hystrixCommandAnnotation = commandMethod.getAnnotation(HystrixCommand.class);
            if (StringUtils.isNotBlank(hystrixCommandAnnotation.fallbackMethod())) {
                Class<?>[] parameterTypes = commandMethod.getParameterTypes();
                Optional<Method> fallbackMethodOptional = getMethod(type, hystrixCommandAnnotation.fallbackMethod(), parameterTypes);
                if (fallbackMethodOptional.isPresent()) {
                    return fallbackMethodOptional.get();
                } else
                    throw new RuntimeException("Fallback method not found: " + hystrixCommandAnnotation.fallbackMethod() + "(" + Arrays.toString(parameterTypes) + ")");
            }
        }
        return null;
    }

    private static Optional<Method> getMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        try {
            return Optional.of(type.getMethod(name, parameterTypes));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }
}
