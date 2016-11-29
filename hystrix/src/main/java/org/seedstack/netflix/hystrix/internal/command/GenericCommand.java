/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GenericCommand extends com.netflix.hystrix.HystrixCommand<Object> {

    private CommandParameters parameters;

    GenericCommand(Setter setter) {
        super(setter);
    }

    void setParameters(CommandParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    protected Object run() throws Exception {
        try {
            return parameters.getInvocation().proceed();
        } catch (Throwable throwable) {
            // propagate the Throwable for Hystrix to catch and execute the fallback
            throw new Exception(throwable.getCause());
        }
    }

    @Override
    protected Object getFallback() {
        try {
            Method fallbackMethod = parameters.getFallbackMethod();
            fallbackMethod.setAccessible(true);
            return fallbackMethod.invoke(parameters.getProxy(), parameters.getArgs());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
