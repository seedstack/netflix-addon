/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal.command;

import com.google.common.base.Strings;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixExecutable;

public class HystrixCommandFactory {

    public static HystrixExecutable create(CommandParameters commandParameters) {
        String groupKey = (Strings.isNullOrEmpty(commandParameters.getHystrixCommand().groupKey()))
                ? commandParameters.getMethod().getName()
                : commandParameters.getHystrixCommand().groupKey();
        String commandKey = (Strings.isNullOrEmpty(commandParameters.getHystrixCommand().commandKey()))
                ? commandParameters.getMethod().getName()
                : commandParameters.getHystrixCommand().commandKey();

        HystrixCommand.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
        GenericCommand genericCommand = new GenericCommand(setter);
        genericCommand.setParameters(commandParameters);
        return genericCommand;
    }
}
