/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.eureka.internal;

import com.google.inject.AbstractModule;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClient;

class EurekaModule extends AbstractModule {
    private final EurekaClient eurekaClient;
    private final ApplicationInfoManager applicationInfoManager;

    EurekaModule(EurekaClient eurekaClient, ApplicationInfoManager applicationInfoManager) {
        this.eurekaClient = eurekaClient;
        this.applicationInfoManager = applicationInfoManager;
    }

    @Override
    protected void configure() {
        bind(EurekaClient.class).toInstance(eurekaClient);
        bind(ApplicationInfoManager.class).toInstance(applicationInfoManager);
    }
}
