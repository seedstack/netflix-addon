/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.feign;

import com.google.inject.Inject;
import org.junit.Test;
import org.seedstack.netflix.feign.fixtures.TestAPI;
import org.seedstack.seed.it.AbstractSeedIT;

import static org.assertj.core.api.Assertions.assertThat;

public class FeignIT extends AbstractSeedIT {

    @Inject
    private TestAPI testAPI;

    @Test
    public void test_API() {
        assertThat(testAPI).isNotNull();
    }
}
