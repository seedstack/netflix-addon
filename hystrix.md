---
title: "Hystrix"
parent: "Netflix"
repo: "https://github.com/seedstack/netflix-addon"
weight: -1
tags:
    - "netflix"
    - "cloud"
    - "microservices"
    - "api"
zones:
    - Addons
menu:
    AddonNetflix:
        weight: 30
---

This component allows you to wrap your network calls with the Hystrix library. Hystrix is a library that add latency tolerance and fault tolerance logic, as well as metrics.
{{< dependency g="org.seedstack.addons.netflix" a="netflix-hystrix" >}}

{{% callout info %}}
For more information on Hystrix: [https://github.com/Netflix/Hystrix/wiki](https://github.com/Netflix/Hystrix/wiki)
{{% /callout %}}

# How to use

Simply annotate your method with `@HystrixCommand`, and you're good to go:
```java
public class MyService {

    @HystrixCommand
    public String helloWorld(String name) {
        // a real service would have a network call here
        return "Hello " + name + "!"; 
    }
}
```

After that, you can use your command by injecting your service and calling the appropriate method:
```java
public class BusinessService {

    @Inject
    MyService service;
    
    public void doSomething() {
        ...
        service.helloWorld("world");
        ...
    }
}
```

To utilize the fallback mecanism, add the name of the fallback method in the annotation:
```java
public class MyService {

    @HystrixCommand(fallbackMethod = "fallback")
    public String helloWorld(String name) {
        // a real service would have a network call here
        return "Hello " + name + "!"; 
    }
    
    public String fallback(String name) {
        return "Hello failure " + name + "!";
    }
}
```

{{% callout warn %}}
It's important to remember that the Hystrix command and the fallback must be in the same class and have the same signature !
{{% /callout %}}

The fallback method could be a Hystrix command as well, but it isn't recommended.

# Asynchronous execution

To process a Hystrix command asynchronously, your return type must be a Future:
```java
public class MyService {

    @HystrixCommand
    public Future<String> helloWorldAsync(String name) {
        // a real service would have a network call here
        ExecutorService executor = ...
        return executor.submit(() -> "Hello async " + name + "!");
    }
}
```

# Parameters in the annotation

You can set the `commandKey` and `groupKey` values in the `@HystrixCommand` annotation.
These values are used to name commands and group of commands for the purpose of reporting, alerting, statistics, etc.
For more information, see the documentation on [Hystrix wiki](https://github.com/Netflix/Hystrix/wiki/How-To-Use#command-name).
