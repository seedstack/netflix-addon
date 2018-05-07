/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.netflix.hystrix.internal;

import static org.seedstack.shed.reflect.AnnotationPredicates.elementAnnotatedWith;

import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.netflix.hystrix.strategy.HystrixPlugins;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kametic.specifications.Specification;
import org.seedstack.netflix.hystrix.HystrixCommand;
import org.seedstack.netflix.hystrix.HystrixConfig;
import org.seedstack.seed.core.SeedRuntime;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;
import org.seedstack.seed.web.spi.FilterDefinition;
import org.seedstack.seed.web.spi.ListenerDefinition;
import org.seedstack.seed.web.spi.ServletDefinition;
import org.seedstack.seed.web.spi.WebProvider;
import org.seedstack.shed.reflect.Classes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HystrixPlugin extends AbstractSeedPlugin implements WebProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixPlugin.class);
    private final Specification<Class<?>> spec = classMethodsAnnotatedWith(HystrixCommand.class);
    private final Collection<Class<?>> bindings = new ArrayList<>();
    private final Map<Method, CommandDefinition> commands = new HashMap<>();
    private HystrixConfig hystrixConfig;
    private boolean metricsPresent;

    @Override
    public String name() {
        return "netflix-hystrix";
    }

    @Override
    protected void setup(SeedRuntime seedRuntime) {
        Classes.optional("com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet")
                .ifPresent(c -> {
                    metricsPresent = true;
                    bindings.add(c);
                });
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder()
                .specification(spec)
                .build();
    }

    @Override
    protected InitState initialize(InitContext initContext) {
        hystrixConfig = getConfiguration(HystrixConfig.class);
        initContext.scannedTypesBySpecification().get(spec)
                .forEach(c -> Classes.from(c)
                        .traversingSuperclasses()
                        .traversingInterfaces()
                        .methods()
                        .filter(m -> !Modifier.isAbstract(m.getModifiers()))
                        .filter(elementAnnotatedWith(HystrixCommand.class, true))
                        .forEach(m -> commands.put(m, new CommandDefinition(m))));

        HystrixPlugins hystrixPlugins = HystrixPlugins.getInstance();

        // Dynamic properties through Coffig
        ((CoffigHystrixDynamicProperties) hystrixPlugins.getDynamicProperties()).setHystrixConfig(hystrixConfig);

        return InitState.INITIALIZED;
    }

    @Override
    public Object nativeUnitModule() {
        return new HystrixModule(bindings, Collections.unmodifiableMap(commands));
    }

    @Override
    public List<ServletDefinition> servlets() {
        ArrayList<ServletDefinition> servletDefinitions = Lists.newArrayList();
        if (metricsPresent && hystrixConfig.metrics().isEnabled()) {
            ServletDefinition servletDefinition = new ServletDefinition(
                    "HystrixMetricsStreamServlet",
                    HystrixMetricsStreamServlet.class);
            servletDefinition.addMappings(hystrixConfig.metrics().getServletPath());
            servletDefinition.setAsyncSupported(true);
            servletDefinitions.add(servletDefinition);
            LOGGER.info("Hystrix metrics stream exposed on " + hystrixConfig.metrics().getServletPath());
        }
        return servletDefinitions;
    }

    @Override
    public List<FilterDefinition> filters() {
        return Lists.newArrayList();
    }

    @Override
    public List<ListenerDefinition> listeners() {
        return Lists.newArrayList();
    }
}
