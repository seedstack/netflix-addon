/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import java.lang.reflect.Method;
import org.seedstack.netflix.hystrix.HystrixCommand;
import org.seedstack.shed.reflect.StandardAnnotationResolver;

class HystrixCommandAnnotationResolver extends StandardAnnotationResolver<Method, HystrixCommand> {
    static HystrixCommandAnnotationResolver INSTANCE = new HystrixCommandAnnotationResolver();

    private HystrixCommandAnnotationResolver() {
        // no external instantiation allowed
    }
}
