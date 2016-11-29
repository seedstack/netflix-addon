/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal.guice;

import org.seedstack.seed.core.internal.AbstractSeedModule;

import java.util.Collection;

import static com.google.inject.matcher.Matchers.any;

class HystrixModule extends AbstractSeedModule {

    private final Collection<Class<?>> scannedClasses;

    HystrixModule(Collection<Class<?>> scannedClasses) {
        this.scannedClasses = scannedClasses;
    }

    @Override
    protected void configure() {
        scannedClasses.forEach(this::bind);
        bindInterceptor(any(), HystrixPlugin.HYSTRIX_COMMAND_MATCHER, new CommandInterceptor());
    }
}
