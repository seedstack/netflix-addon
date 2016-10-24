/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.feign.internal;

import org.seedstack.seed.core.internal.AbstractSeedModule;

import java.util.Collection;

class FeignModule extends AbstractSeedModule {
    private final Collection<Class<FeignApi>> feignApis;

    FeignModule(Collection<Class<FeignApi>> feignApis) {
        this.feignApis = feignApis;
    }

    @Override
    protected void configure() {

        for (Class<FeignApi> feignApi : feignApis) {
            bind(feignApi).toProvider(new FeignProvider(feignApi));
        }
    }


}
