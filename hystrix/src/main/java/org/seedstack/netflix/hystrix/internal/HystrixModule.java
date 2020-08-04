/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import static com.google.inject.matcher.Matchers.any;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import org.seedstack.seed.core.internal.utils.MethodMatcherBuilder;

class HystrixModule extends AbstractModule {
    private static final Matcher<Method> HYSTRIX_COMMAND_MATCHER =
            new MethodMatcherBuilder(HystrixCommandAnnotationResolver.INSTANCE).build();
    private final Collection<Class<?>> scannedClasses;
    private final Map<Method, CommandDefinition> commands;

    HystrixModule(Collection<Class<?>> scannedClasses, Map<Method, CommandDefinition> commands) {
        this.scannedClasses = scannedClasses;
        this.commands = commands;
    }

    @Override
    protected void configure() {
        scannedClasses.forEach(this::bind);
        bindInterceptor(any(), HYSTRIX_COMMAND_MATCHER, new CommandInterceptor(commands));
    }
}
