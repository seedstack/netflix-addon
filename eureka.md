---
title: "Eureka"
addon: "Netflix"
repo: "https://github.com/seedstack/netflix-addon"
weight: -1
tags:
    - api
    - micro-service
zones:
    - Addons
menu:
    Netflix:
        parent: "contents"
        weight: 40
---

This component is used to discover services for the purpose of load balancing and failover of middle-tier servers.<!--more-->

{{< dependency g="org.seedstack.addons.netflix" a="netflix-eureka" >}}

{{% callout info %}}
For more information on Eureka: [https://github.com/Netflix/eureka/wiki](https://github.com/Netflix/eureka/wiki)
{{% /callout %}}

# How to use

First, you need a Eureka server running. This server will be used by your clients to contact the right services.
See this link to learn how to configure and build the server : [https://github.com/Netflix/eureka/wiki/Building-Eureka-Client-and-Server](https://github.com/Netflix/eureka/wiki/Building-Eureka-Client-and-Server)

Then, you can inject the object `EurekaClient` in your code, which will have been created from the configuration.
```java
public class MyService {
    @Inject
    private EurekaClient eurekaClient;

    public void service() {
        String vipAddress = "myservice.mydomain.net";
        InstanceInfo nextServerInfo = null;

        while(nextServerInfo == null) {
            try {
                nextServerInfo = this.eurekaClient.getNextServerFromEureka(vipAddress, false);
            } catch (Throwable t) {
                System.out.println("Waiting ... verifying service registration with eureka ...");
                Thread.sleep(10000L);
            }
        }
        // do something with nextServerInfo
        ...
    }
}
```

# Configuration

You can configure the two objects used in an Eureka architecture that are the service and the client.
To configure your instance of the service:
```ini
eureka:
  instance:
    appname: sampleegisteringService
    virtualHostName: myservice.mydomain.net
```

To configure your client:
```ini
eureka:
  client:
    shouldPreferSameZoneEureka: true
    shouldUseDnsForFetchingServiceUrls: false
    eurekaServerServiceUrls: http://localhost:8080/eureka/v2/
    decoderName: JacksonJson
    region: default
```

A lot of options are available, please refer to [EurekaConfig.java](https://github.com/seedstack/netflix-addon/blob/eureka/eureka/src/main/java/org/seedstack/netflix/eureka/EurekaConfig.java) for more information.
