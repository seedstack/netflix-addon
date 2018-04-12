/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.fixtures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.seedstack.netflix.hystrix.HystrixCommand;
import org.seedstack.seed.Bind;
import rx.Observable;

@Bind
public class CommandHelloWorld {

    @HystrixCommand(commandKey = "testCommand", groupKey = "testGroup", fallbackMethod = "fallback2")
    public String helloWorld(String name, boolean fail) {
        if (fail) {
            throw new IllegalArgumentException("I'm failing");
        }
        return "Hello " + name + " !";
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public String failure(String name) {
        throw new RuntimeException("This method always fails !");
    }

    private String fallback(String name) {
        return "Fallback : Hello " + name + " !";
    }

    private String fallback2(String name, boolean fail) {
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
