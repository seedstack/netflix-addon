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
import org.seedstack.netflix.hystrix.internal.annotation.HystrixCommand;
import org.seedstack.netflix.hystrix.internal.command.CommandParameters;
import org.seedstack.netflix.hystrix.internal.command.HystrixCommandFactory;
import org.seedstack.netflix.hystrix.internal.utils.MethodUtils;
import rx.Observable;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

class CommandInterceptor implements MethodInterceptor {

    /**
     * Wraps the intercepted method into a hystrix command and executes it
     *
     * @param invocation the method invocation joinpoint
     * @return the result of the intercepted method
     * @throws Throwable if the interceptors or the target-object throws an exception
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method command = invocation.getMethod();
        HystrixCommand hystrixCommand = command.getDeclaredAnnotation(HystrixCommand.class);
        Class<?> declaringClassOfCommand = command.getDeclaringClass();
        CommandParameters commandParameters = new CommandParameters(
                hystrixCommand,
                command,
                MethodUtils.getFallbackMethod(declaringClassOfCommand, command),
                invocation.getArguments(),
                invocation,
                invocation.getThis());
        HystrixExecutable executable = HystrixCommandFactory.create(commandParameters);
        Class<?> returnType = command.getReturnType();
        if (Future.class.isAssignableFrom(returnType)) {
            return executable.queue();
        } else if (Observable.class.isAssignableFrom(returnType)) {
            return executable.observe();
        } else {
            return executable.execute();
        }
    }
}
