/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal.command;

import org.aopalliance.intercept.MethodInvocation;
import org.seedstack.netflix.hystrix.internal.annotation.HystrixCommand;

import java.lang.reflect.Method;

public class CommandParameters {
    private final HystrixCommand hystrixCommand;
    private final Method method;
    private final Method fallbackMethod;
    private final Object[] args;
    private final MethodInvocation invocation;
    private final Object proxy;

    public CommandParameters(HystrixCommand hystrixCommand, Method method, Method fallbackMethod, Object[] args, MethodInvocation invocation, Object proxy) {
        this.hystrixCommand = hystrixCommand;
        this.method = method;
        this.fallbackMethod = fallbackMethod;
        this.args = args;
        this.invocation = invocation;
        this.proxy = proxy;
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

    public Object[] getArgs() {
        return args;
    }

    public MethodInvocation getInvocation() {
        return invocation;
    }

    public Object getProxy() {
        return proxy;
    }
}
