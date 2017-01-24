/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.junit.Test;
import org.seedstack.seed.it.AbstractSeedIT;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class EurekaIT extends AbstractSeedIT {

    @Inject
    private EurekaClient eurekaClient;

    @Test
    public void eurekaClientIsRunningCorrectly() throws Exception {
        assertThat(eurekaClient).isNotNull();

        // initialize the client
        // this is the vip address for the example service to talk to as defined in conf/sample-eureka-service.properties
        String vipAddress = "sampleservice.mydomain.net";

        InstanceInfo nextServerInfo = null;
        nextServerInfo = eurekaClient.getNextServerFromEureka(vipAddress, false);

        System.out.println("Found an instance of example service to talk to from eureka: "
                + nextServerInfo.getVIPAddress() + ":" + nextServerInfo.getPort());

        System.out.println("healthCheckUrl: " + nextServerInfo.getHealthCheckUrl());
        System.out.println("override: " + nextServerInfo.getOverriddenStatus());

        Socket s = new Socket();
        int serverPort = nextServerInfo.getPort();
        s.connect(new InetSocketAddress(nextServerInfo.getHostName(), serverPort));
        String request = "FOO " + new Date();
        System.out.println("Connected to server. Sending a sample request: " + request);

        PrintStream out = new PrintStream(s.getOutputStream());
        out.println(request);

        System.out.println("Waiting for server response..");
        BufferedReader rd = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String str = rd.readLine();
        if (str != null) {
            System.out.println("Received response from server: " + str);
            assertThat(str).startsWith("BAR");
            System.out.println("Exiting the client. Demo over..");
        }
        rd.close();
    }
}
