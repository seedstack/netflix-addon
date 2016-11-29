/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix;

import com.google.inject.Inject;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Test;
import org.seedstack.netflix.hystrix.fixtures.CommandHelloWorld;
import org.seedstack.seed.it.AbstractSeedIT;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class HystrixIT extends AbstractSeedIT {

    @Inject
    CommandHelloWorld command;

    @Test
    public void commandIsInjected() {
        assertThat(command).isNotNull();
    }

    @Test
    public void commandExecutesCorrectly() {
        assertThat(command.helloWorld("test")).isEqualTo("Hello test !");
    }

    @Test
    public void commandExecutesFallback() throws Exception {
        assertThat(command.failure("foo")).isEqualTo("Fallback : Hello foo !");
    }

    @Test
    public void nestedFallbacksAreSuccessful() throws Exception {
        assertThat(command.nestedCommand("bar")).isEqualTo("nestedFallback2: Hello bar !");

    }

    @Test
    public void fallbackNotFound() throws Exception {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> command.fallbackError(""))
                .withMessage("Fallback method not found: inexistantFallback([class java.lang.String])");
    }

    @Test
    public void fallbackThrowsAnException() throws Exception {
        assertThatExceptionOfType(HystrixRuntimeException.class)
                .isThrownBy(() -> command.commandWithFallbackInException(""))
                .withMessage("commandWithFallbackInException failed and fallback failed.");
    }
}
