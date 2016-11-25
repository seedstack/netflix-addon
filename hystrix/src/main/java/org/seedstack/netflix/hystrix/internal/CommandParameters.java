/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class CommandParameters {
    private final HystrixCommand hystrixCommand;
    private final Method method;
    private final Method fallbackMethod;
    private final Object obj;
    private final Object[] args;
    private final MethodInvocation invocation;

    public CommandParameters(HystrixCommand hystrixCommand, Method method, Method fallbackMethod, Object obj, Object[] args, MethodInvocation invocation) {
        this.hystrixCommand = hystrixCommand;
        this.method = method;
        this.fallbackMethod = fallbackMethod;
        this.obj = obj;
        this.args = args;
        this.invocation = invocation;
    }

    public HystrixCommand getHystrixCommand() {
        return hystrixCommand;
    }

    public Method getMethod() {
        return method;
    }

    public Method getFallbackMethod() {
        return fallbackMethod;
    }

    public Object getObj() {
        return obj;
    }

    public Object[] getArgs() {
        return args;
    }

    public MethodInvocation getInvocation() {
        return invocation;
    }
}
