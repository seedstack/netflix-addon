/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import com.netflix.hystrix.HystrixExecutable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class CommandInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method command = invocation.getMethod();
        Class<?> declaringClassOfCommand = command.getDeclaringClass();
        CommandParameters commandParameters = new CommandParameters(command.getAnnotation(HystrixCommand.class),
                command,
                MethodUtils.getFallbackMethod(declaringClassOfCommand, command),
                declaringClassOfCommand,
                invocation.getArguments(),
                invocation);
        HystrixExecutable executable = HystrixCommandFactory.create(commandParameters);
        return executable.execute();
    }
}
