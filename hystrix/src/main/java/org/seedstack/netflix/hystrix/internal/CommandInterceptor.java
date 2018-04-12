/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.netflix.hystrix.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import rx.Observable;

class CommandInterceptor implements MethodInterceptor {
    private final Map<Method, CommandDefinition> commands;

    CommandInterceptor(Map<Method, CommandDefinition> commands) {
        this.commands = commands;
    }

    @Override
    public Object invoke(MethodInvocation invocation) {
        CommandDefinition commandDefinition = checkNotNull(commands.get(invocation.getMethod()),
                "Unable to find command definition for method " + invocation.getMethod().getName());

        GenericCommand genericCommand = new GenericCommand(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandDefinition.getGroupKey()))
                        .andCommandKey(HystrixCommandKey.Factory.asKey(commandDefinition.getCommandKey())),
                invocation,
                commandDefinition.getFallbackMethod()
        );

        Class<?> returnType = commandDefinition.getCommandMethod().getReturnType();
        if (Future.class.isAssignableFrom(returnType)) {
            return genericCommand.queue();
        } else if (Observable.class.isAssignableFrom(returnType)) {
            return genericCommand.observe();
        } else {
            return genericCommand.execute();
        }
    }
}
