/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.feign;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.seedstack.netflix.feign.fixtures.Message;
import org.seedstack.netflix.feign.fixtures.TestAPI;
import org.seedstack.netflix.feign.fixtures.TestResource;
import org.seedstack.netflix.feign.internal.FeignConfig;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.it.AbstractSeedWebIT;

import javax.inject.Inject;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class FeignIT extends AbstractSeedWebIT {

    @ArquillianResource
    private URL baseUrl;

    @Inject
    private TestAPI testAPI;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(TestResource.class.getPackage());
    }

    @Test
    public void test_API() {
        assertThat(testAPI).isNotNull();
        Message message = testAPI.getMessage();
        assertThat(message.getBody()).isEqualTo("Hello World !");
        assertThat(message.getAuthor()).isEqualTo("computer");
    }

    @Test
    public void test_fallback() {
        Message message = testAPI.get404();
        assertThat(message.getBody()).isEqualTo("Error code: 404 !");
        assertThat(message.getAuthor()).isEqualTo("fallback");
    }
}
