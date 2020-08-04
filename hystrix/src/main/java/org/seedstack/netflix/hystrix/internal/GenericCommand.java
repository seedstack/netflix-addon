/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import static org.seedstack.shed.reflect.ReflectUtils.invoke;
import static org.seedstack.shed.reflect.ReflectUtils.makeAccessible;

import java.lang.reflect.Method;
import java.util.concurrent.Future;
import org.aopalliance.intercept.MethodInvocation;
import rx.Observable;

class GenericCommand extends com.netflix.hystrix.HystrixCommand<Object> {
    private final MethodInvocation methodInvocation;
    private final Method fallbackMethod;

    GenericCommand(Setter setter, MethodInvocation methodInvocation, Method fallbackMethod) {
        super(setter);
        this.methodInvocation = methodInvocation;
        this.fallbackMethod = fallbackMethod != null ? makeAccessible(fallbackMethod) : null;
    }

    @Override
    protected Object run() throws Exception {
        try {
            Object result = methodInvocation.proceed();
            Class<?> returnType = methodInvocation.getMethod().getReturnType();
            if (Future.class.isAssignableFrom(returnType)) {
                return ((Future) result).get();
            } else if (Observable.class.isAssignableFrom(returnType)) {
                // FIXME : works only for Observable that return a single value
                return ((Observable) result).toBlocking().toFuture().get();
            } else {
                return result;
            }
        } catch (Throwable throwable) {
            // propagate the Throwable for Hystrix to catch and execute the fallback
            throw new Exception(throwable);
        }
    }

    @Override
    protected Object getFallback() {
        if (fallbackMethod != null) {
            return invoke(fallbackMethod, methodInvocation.getThis(), methodInvocation.getArguments());
        } else {
            return super.getFallback();
        }
    }
}
