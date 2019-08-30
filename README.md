[![Build Status](https://travis-ci.org/bootique-examples/bootique-metrics-demo.svg)](https://travis-ci.org/bootique-examples/bootique-metrics-demo)
# bootique-metrics-demo

The example of [Dropwizard Metrics](http://metrics.dropwizard.io/3.2.3/) integration into an application built on [Bootique](https://bootique.io).

*For additional help/questions about this example send a message to     
[Bootique forum](https://groups.google.com/forum/#!forum/bootique-user).*
   
## Prerequisites
      
    * Java 1.8 or newer.
    * Apache Maven.
      
## Build the Demo
      
Here is how to build it:
```bash
git@github.com:bootique-examples/bootique-metrics-demo.git
cd bootique-metrics-demo
mvn package
```

## Run the Demo 

Check the options available in your app:
```bash
java -jar target/bootique-metrics-demo-1.0-SNAPSHOT.jar -h  
```
```
OPTIONS
      -c yaml_location, --config=yaml_location
           Specifies YAML config location, which can be a file path or a URL.

      -h, --help
           Prints this message.

      -H, --help-config
           Prints information about application modules and their configuration options.
```
As a sample, let's measure the behavior of data sources. First, configure `data sources` and `metrics` in *config.yml*:
```yaml
jdbc:
  ds1:
    url: jdbc:derby:target/derby/ds1;create=true
    driverClassName: org.apache.derby.jdbc.EmbeddedDriver
    initialSize: 1
  ds2:
    url: jdbc:derby:target/derby/ds2;create=true
    driverClassName: org.apache.derby.jdbc.EmbeddedDriver
    initialSize: 1
metrics:
  reporters:
    - type: sl4j
      period: 5s
```
Second, contribute `DataSourceHealthCheckGroup` combining [Health Checks](http://metrics.dropwizard.io/3.2.3/manual/healthchecks.html)
into a unit of work. 
Third, inject `MetricRegistry` in the code to create [Meters](http://metrics.dropwizard.io/3.1.0/getting-started/#meters), 
[Gauges](http://metrics.dropwizard.io/3.2.3/manual/core.html#gauges), 
[Counters](http://metrics.dropwizard.io/3.1.0/getting-started/#counters), [Histograms](http://metrics.dropwizard.io/3.1.0/getting-started/#histograms), 
[Timers](http://metrics.dropwizard.io/3.1.0/getting-started/#timers), etc. E.g. in the demo:
`Gauges` are registered for connection pools. 

Run the demo: 
```bash
java -jar target/bootique-metrics-demo-1.0-SNAPSHOT.jar --config=config.yml 
```
Result: the default command `ds-check` runs all health checks, [Slf4jReporter](http://metrics.dropwizard.io/3.1.0/manual/core/#man-core-reporters-slf4j) 
prints `Gauges`:
```
INFO  [2017-09-19 12:42:29,536] main i.b.m.d.DataSourceHealthCheckCommand: ds2: OK
INFO  [2017-09-19 12:42:29,537] main i.b.m.d.DataSourceHealthCheckCommand: ds1: OK
INFO  [2017-09-19 12:42:30,142] metrics-logger-reporter-1-thread-1 metrics: type=GAUGE, name=io.bootique.metrics.demo.ds.InstrumentedLazyDataSourceFactory.ds1.active, value=0
INFO  [2017-09-19 12:42:30,143] metrics-logger-reporter-1-thread-1 metrics: type=GAUGE, name=io.bootique.metrics.demo.ds.InstrumentedLazyDataSourceFactory.ds1.idle, value=1
INFO  [2017-09-19 12:42:30,143] metrics-logger-reporter-1-thread-1 metrics: type=GAUGE, name=io.bootique.metrics.demo.ds.InstrumentedLazyDataSourceFactory.ds1.size, value=1
INFO  [2017-09-19 12:42:30,143] metrics-logger-reporter-1-thread-1 metrics: type=GAUGE, name=io.bootique.metrics.demo.ds.InstrumentedLazyDataSourceFactory.ds1.waiting, value=0
INFO  [2017-09-19 12:42:30,143] metrics-logger-reporter-1-thread-1 metrics: type=GAUGE, name=io.bootique.metrics.demo.ds.InstrumentedLazyDataSourceFactory.ds2.active, value=0
INFO  [2017-09-19 12:42:30,143] metrics-logger-reporter-1-thread-1 metrics: type=GAUGE, name=io.bootique.metrics.demo.ds.InstrumentedLazyDataSourceFactory.ds2.idle, value=1
...
```








    
    






        
        
     