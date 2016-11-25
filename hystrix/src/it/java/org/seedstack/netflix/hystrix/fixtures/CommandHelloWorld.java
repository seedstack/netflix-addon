/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.fixtures;

import org.seedstack.netflix.hystrix.internal.HystrixCommand;
import org.seedstack.seed.it.ITBind;

public class CommandHelloWorld {

    @HystrixCommand(fallbackMethod = "helloWorldFallback")
    public String helloWorld(String name) {
        if (!name.equals("error")) {
            return "Hello " + name + " !";
        } else {
            throw new RuntimeException("Failed !");
        }
    }

    public String helloWorldFallback(String name) {
        return "Fallback : Hello " + name + " !";
    }
}
