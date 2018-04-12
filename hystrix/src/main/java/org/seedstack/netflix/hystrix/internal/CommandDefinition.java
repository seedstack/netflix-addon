/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.netflix.hystrix.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.seedstack.netflix.hystrix.HystrixCommand;
import org.seedstack.shed.reflect.Classes;

class CommandDefinition {
    private final Method commandMethod;
    private final Method fallbackMethod;
    private final String groupKey;
    private final String commandKey;

    CommandDefinition(Method commandMethod) {
        this.commandMethod = checkNotNull(commandMethod, "Missing command method");
        HystrixCommand hystrixCommand = HystrixCommandAnnotationResolver.INSTANCE
                .apply(commandMethod)
                .orElseThrow(() -> new IllegalArgumentException("Missing @HystrixCommand annotation on method "
                        + commandMethod));

        this.fallbackMethod = resolveFallbackMethod(hystrixCommand);
        this.groupKey = resolveGroupKey(hystrixCommand);
        this.commandKey = resolveCommandKey(hystrixCommand);
    }

    Method getCommandMethod() {
        return commandMethod;
    }

    Method getFallbackMethod() {
        return fallbackMethod;
    }

    String getGroupKey() {
        return groupKey;
    }

    String getCommandKey() {
        return commandKey;
    }

    private String resolveGroupKey(HystrixCommand hystrixCommand) {
        return Strings.isNullOrEmpty(hystrixCommand.groupKey())
                ? commandMethod.getDeclaringClass().getName() : hystrixCommand.groupKey();
    }

    private String resolveCommandKey(HystrixCommand hystrixCommand) {
        return Strings.isNullOrEmpty(hystrixCommand.commandKey())
                ? commandMethod.getName() : hystrixCommand.commandKey();
    }

    private Method resolveFallbackMethod(HystrixCommand hystrixCommand) {
        if (StringUtils.isNotBlank(hystrixCommand.fallbackMethod())) {
            Class<?>[] parameterTypes = commandMethod.getParameterTypes();
            return Classes.from(commandMethod.getDeclaringClass())
                    .traversingSuperclasses()
                    .traversingInterfaces()
                    .method(hystrixCommand.fallbackMethod(), parameterTypes)
                    .orElseThrow(() -> new RuntimeException("No fallback method found: "
                            + hystrixCommand.fallbackMethod() + "(" + Arrays.toString(parameterTypes) + ")"));
        }
        return null;
    }
}
