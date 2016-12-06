/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.fixtures;

import org.seedstack.netflix.hystrix.internal.annotation.HystrixCommand;
import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.*;

public class CommandHelloWorld {

    @HystrixCommand(commandKey = "testCommand", groupKey = "testGroup")
    public String helloWorld(String name) {
        return "Hello " + name + " !";
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public String failure(String name) {
        throw new RuntimeException("This method always fails !");
    }

    private String fallback(String name) {
        return "Fallback : Hello " + name + " !";
    }

    @HystrixCommand(fallbackMethod = "nestedFallback1")
    public String nestedCommand(String name) {
        throw new RuntimeException("Fail !");
    }

    @HystrixCommand(fallbackMethod = "nestedFallback2")
    public String nestedFallback1(String name) {
        throw new RuntimeException("Fallback fail !");
    }

    public String nestedFallback2(String name) {
        return "nestedFallback2: Hello " + name + " !";
    }

    @HystrixCommand(fallbackMethod = "inexistantFallback")
    public String fallbackError(String name) {
        throw new RuntimeException("Fail, and fallback doesn't exist !");
    }

    @HystrixCommand(fallbackMethod = "fallbackInException")
    public String commandWithFallbackInException(String name) {
        throw new RuntimeException("Fail, and fallback will fail !");
    }

    public String fallbackInException(String name) {
        throw new RuntimeException("Error in fallback");
    }

    @HystrixCommand
    public Future<String> asyncCommand(String name) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> "Async: Hello " + name + " !");
    }

    @HystrixCommand
    public Observable<String> observableCommand(String name) {
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext("Observer: Hello " + name + " !");
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
