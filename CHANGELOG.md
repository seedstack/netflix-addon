# Version 1.2.2 (2020-08-05)

* [chg] Updated Hystrix to 1.5.18.

# Version 1.2.1 (2018-05-07)

* [new] Fix class not found when `hystrix-metrics-event-stream` JAR is not on the classpath. 

# Version 1.2.0 (2018-05-07)

* [new] Support for metrics event stream servlet, allowing to connect the Hystrix dashboard to the application.
* [chg] Commands are now detected at startup, failing fast if a problem is detected.

# Version 1.1.0 (2017-11-24)

* [new] [Hystrix properties](https://github.com/Netflix/Hystrix/wiki/Configuration) can now be set directly in SeedStack configuration. 
* [chg] Updated Hystrix to 1.5.12
* [chg] Remove unneeded dependency to Netflix Archaius in Hystrix module.  

# Version 1.0.1 (2017-05-02)

* [chg] Feign has been moved to its own [add-on](https://github.com/seedstack/feign-addon).

# Version 1.0.0 (2017-01-03)

* [new] Initial release.
