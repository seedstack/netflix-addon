/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.netflix.hystrix.fixtures;

import javax.inject.Inject;
import org.seedstack.seed.Ignore;
import org.seedstack.seed.LifecycleListener;

@Ignore // Uncomment to test the dashboard with some data
public class InitLifecycleListener implements LifecycleListener {
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
                commandHelloWorld.asyncCommand("World");
                commandHelloWorld.observableCommand("World");
                try {
                    Thread.sleep((long) (Math.random() * 1000));
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
