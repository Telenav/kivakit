![](https://www.kivakit.org/images/horizontal-line.png)

## Version 1.1.2-SNAPSHOT (2021.08.27) "plutonium snake"

### added

* New mini-frameworks  
  * *kivakit-microservices* - Jetty, REST, GRPC, Swagger microservices  
  * *kivakit-metrics* - Metrics collection  
  * *kivakit-metrics-prometheus* - Prometheus metrics provider
  * *kivakit-filesystems-java* - Java FileSystem provider
  * *kivakit-data-formats-xml* - StaxReader to improve use of java.xml.stream API

### updated

* Refactoring to improve naming and packaging
* Added and improved documentation
* Static methods returning broadcasting components now take a Listener as the first argument.
  This prevents callers from accidentally forgetting to *listenTo()* the component.
* Maven pom files now inherit from a "superpom" 
* Divided Component interface into traits:
    * RegistryTrait
    * SettingsTrait
    * ResourceTrait
    * TryTrait

### fixed

* Fixes to build and deployment scripts
* Many small bug fixes

![](https://www.kivakit.org/images/horizontal-line.png)

## Version 1.1.2-SNAPSHOT (2021.08.27) "puffy mouse"

Initial release of KivaKit.

![](https://www.kivakit.org/images/horizontal-line.png)

