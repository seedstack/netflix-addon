---
title: "Hystrix"
addon: "Netflix"
repo: "https://github.com/seedstack/netflix-addon"
weight: -1
tags:
    - micro-service
    - REST
    - API
zones:
    - Addons
menu:
    Netflix:
        parent: "contents" 
        weight: 30
---

This component allows you to wrap your network calls with the Hystrix library. Hystrix is a library that add latency tolerance 
and fault tolerance logic, as well as metrics.<!--more-->

{{< dependency g="org.seedstack.addons.netflix" a="netflix-hystrix" >}}

{{% callout info %}}
For more information on Hystrix: [https://github.com/Netflix/Hystrix/wiki](https://github.com/Netflix/Hystrix/wiki)
{{% /callout %}}

## Configuration

All Hystrix configuration is based on properties that can be specified in SeedStack configuration:

{{% config p="netflix.hystrix" %}}
```yaml
netflix:
  hystrix:
    properties:
      threadpool.default.coreSize: 10
      threadpool.default.maxQueueSize: 100
      threadpool.default.queueSizeRejectionThreshold: 50
      ...
```
{{% /config %}}

{{% callout info %}}
Note that the `hystrix.` prefix found in all properties from the Hystrix documentation must be removed.
{{% /callout %}}

## Usage

Simply annotate a method with `@HystrixCommand`, and it will automatically be wrapped in an Hystrix command. Hystrix command are
often used in [business services]({{< ref "docs/business/services.md" >}}) implementations depending on the network:

```java
@Service
public interface SomeService {
	String sayHello(String name);
}
```

And: 
  
```java
public class SomeServiceImpl implements SomeService {
    @HystrixCommand
    public String sayHello(String name) {
        // A real service would have a network call here
        return "Hello " + name + "!"; 
    }
}
```

{{% callout warning %}}
Method interception is used to wrap the method in an Hystrix command. Beware of SeedStack 
[method interception limitations]({{< ref "docs/basics/dependency-injection.md#method-interception" >}}).  
{{% /callout %}}

The method can then be used like usual in other parts of the application:

```java
public class SomeClass {
    @Inject
    private SomeService someService;
    
    public void doSomething() {
        someService.sayHello("world");
    }
}
```

### Fallback

To make use of the Hystrix fallback mecanism, add a fallback method to the same class and reference its name from the annotation:

```java
public class SomeServiceImpl implements SomeService {
    @HystrixCommand(fallbackMethod = "sayHelloFallback")
    public String sayHello(String name) {
        // a real service would have a network call here
        return "Hello " + name + "!"; 
    }
    
    public String sayHelloFallback(String name) {
        return "Failed to say hello to " + name + "!";
    }
}
```

{{% callout warning %}}
The nominal and fallback methods must be in the same class and have the same signature. The fallback method could be a Hystrix command as well, 
but this is not recommended.
{{% /callout %}}

### Asynchronous execution

To process a Hystrix command asynchronously, your return type must be a {{< java "java.util.concurrent.Future" >}}:
```java
public class SomeServiceAsyncImpl implements SomeService {
    @HystrixCommand
    public Future<String> sayHello(String name) {
        // a real service would have a network call here
        ExecutorService executor = ...
        return executor.submit(() -> "Hello async " + name + "!");
    }
}
```

### Annotation parameters

You can set the `commandKey` and `groupKey` values in the `@HystrixCommand` annotation. These values are used to name commands and 
group of commands for the purpose of configuration, reporting, alerting, statistics, etc. For more information, see the documentation 
on [Hystrix wiki](https://github.com/Netflix/Hystrix/wiki/How-To-Use#command-name).
