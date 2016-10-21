/*
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.feign.internal;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import org.kametic.specifications.Specification;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class FeignPlugin extends AbstractSeedPlugin {
    private static final Specification<Class<?>> feignInterfaceSpecification = new FeignInterfaceSpecification();

    private Collection<Class<FeignApi>> feignApis = new ArrayList<>();

    @Override
    public String name() {
        return "feign";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder()
                .specification(feignInterfaceSpecification)
                .build();
    }

    @Override
    protected InitState initialize(InitContext initContext) {
        Map<Specification, Collection<Class<?>>> scannedClasses = initContext.scannedTypesBySpecification();

        configureFeignApi(scannedClasses.get(feignInterfaceSpecification));

        return InitState.INITIALIZED;
    }

    @Override
    public Object nativeUnitModule() {
        return new FeignModule(feignApis);
    }

    @SuppressWarnings("unchecked")
    private void configureFeignApi(Collection<Class<?>> apiClasses) {
        if (apiClasses != null) {
            this.feignApis.addAll(apiClasses.stream().filter(FeignApi.class::isAssignableFrom).map(apiCandidateClass -> (Class<FeignApi>) apiCandidateClass).collect(Collectors.toList()));
        }
    }
}
