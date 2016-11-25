/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal;

import com.google.inject.matcher.Matcher;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import org.kametic.specifications.Specification;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;
import org.seedstack.seed.core.utils.SeedMatchers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class HystrixPlugin extends AbstractSeedPlugin {
    private Collection<Class<?>> scannedClasses = new ArrayList<>();
    static final Matcher<Method> HYSTRIX_COMMAND_MATCHER = SeedMatchers.methodOrAncestorMetaAnnotatedWith(HystrixCommand.class).and(SeedMatchers.methodNotSynthetic()).and(SeedMatchers.methodNotOfObject());
    private Specification<Class<?>> hystrixCommandSpecification = classMethodsAnnotatedWith(HystrixCommand.class);

    @Override
    public String name() {
        return "hystrix";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder()
                .specification(hystrixCommandSpecification)
                .build();
    }

    @Override
    protected InitState initialize(InitContext initContext) {
        Map<Specification, Collection<Class<?>>> scannedClassesBySpecification = initContext.scannedTypesBySpecification();
        scannedClasses.addAll(scannedClassesBySpecification.get(hystrixCommandSpecification));
        return super.initialize(initContext);
    }

    @Override
    public Object nativeUnitModule() {
        return new HystrixModule(scannedClasses);
    }
}
