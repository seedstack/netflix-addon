/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix;

import org.junit.Test;
import org.seedstack.netflix.hystrix.fixtures.CommandHelloFailure;
import org.seedstack.netflix.hystrix.fixtures.CommandHelloWorld;
import org.seedstack.seed.it.AbstractSeedIT;

import static org.assertj.core.api.Assertions.assertThat;

public class HystrixIT extends AbstractSeedIT {

    @Test
    public void test_command() {
        assertThat(new CommandHelloWorld("World").execute()).isEqualTo("Hello World !");
    }

    @Test
    public void test_fallback() throws Exception {
        assertThat(new CommandHelloFailure("World").execute()).isEqualTo("Hello failure World !");
        assertThat(new CommandHelloFailure("Bob").execute()).isEqualTo("Hello failure Bob !");
    }
}
