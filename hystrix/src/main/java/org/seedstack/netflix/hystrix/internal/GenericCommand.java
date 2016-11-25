/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

public class GenericCommand extends com.netflix.hystrix.HystrixCommand<Object> {

    protected CommandParameters parameters;

    protected GenericCommand(Setter setter) {
        super(setter);
    }

    public CommandParameters getParameters() {
        return parameters;
    }

    public void setParameters(CommandParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    protected Object run() throws Exception {
        try {
            return parameters.getInvocation().proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
