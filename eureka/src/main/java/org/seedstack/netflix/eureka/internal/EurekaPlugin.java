/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.eureka.internal;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import org.seedstack.netflix.eureka.EurekaConfig;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;

public class EurekaPlugin extends AbstractSeedPlugin {
    private EurekaClient eurekaClient;
    private ApplicationInfoManager applicationInfoManager;

    @Override
    public String name() {
        return "eureka";
    }

    @Override
    protected InitState initialize(InitContext initContext) {
        EurekaConfig eurekaConfig = getConfiguration(EurekaConfig.class);
        EurekaConfig.InstanceConfig instanceConfig = eurekaConfig.instance();
        EurekaConfig.ClientConfig clientConfig = eurekaConfig.client();

        InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
        applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.STARTING);
        eurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);

        return InitState.INITIALIZED;
    }

    @Override
    public void stop() {
        if (eurekaClient != null) {
            eurekaClient.shutdown();
        }
    }

    @Override
    public Object nativeUnitModule() {
        return new EurekaModule(eurekaClient, applicationInfoManager);
    }
}
