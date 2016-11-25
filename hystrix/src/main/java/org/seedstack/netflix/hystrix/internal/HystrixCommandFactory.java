/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixExecutable;

public class HystrixCommandFactory {

    public static HystrixExecutable create(CommandParameters commandParameters) {
        // TODO : change groupKey to what is in the annotation is it exists
        HystrixCommand.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandParameters.getMethod().getName()));
        GenericCommand genericCommand = new GenericCommand(setter);
        genericCommand.setParameters(commandParameters);
        return genericCommand;
    }
}
