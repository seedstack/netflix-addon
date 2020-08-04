/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix;

import org.seedstack.netflix.hystrix.fixtures.CommandHelloWorld;
import org.seedstack.seed.LifecycleListener;
import org.seedstack.seed.core.Seed;

import javax.inject.Inject;

/**
 * This main class invokes three operations wrapped in Hystrix commands.
 * A 10% failing rate is faked for the "helloWorld()" command.
 *
 * <p>
 * The Hystrix data can be streamed at http://localhost:8080/hystrix.stream
 * </p>
 * <p>
 * To try the popular Hystrix dashboard, download the standalone JAR at
 * https://search.maven.org/remotecontent?filepath=com/github/kennedyoliveira/standalone-hystrix-dashboard/1.5.3/standalone-hystrix-dashboard-1.5.3-all.jar
 * </p>
 * <p>
 * Run it with the {@code java -jar standalone-hystrix-dashboard-1.5.3-all.jar} command and add the stream described
 * above to it.
 * </p>
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        Seed.getLauncher().launch(args);
    }

    static class InitLifecycleListener implements LifecycleListener {
        @Inject
        private CommandHelloWorld commandHelloWorld;
        private boolean stop;
        private Thread thread;

        @Override
        public void started() {
            thread = new Thread(() -> {
                while (!stop) {
                    boolean fail = ((int) (Math.random() * 1000)) % 10 == 0;
                    if (fail) {
                        System.out.println("Invoking failing command...");
                    } else {
                        System.out.println("Invoking command...");
                    }
                    commandHelloWorld.helloWorld("World", fail);
                    try {
                        commandHelloWorld.asyncCommand("World");
                        commandHelloWorld.observableCommand("World");
                    } catch (Exception e) {
                        // no fallback for async and observable commands so we should catch potential exceptions
                    }
                    try {
                        Thread.sleep((long) (Math.random() * 100));
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
                System.out.println("Stopping");
            }, "trigger");
            thread.start();
        }

        @Override
        public void stopping() {
            stop = true;
            thread.interrupt();
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
