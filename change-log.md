![](https://www.kivakit.org/images/horizontal-line.png)

## Version 1.1.1 (2021.11.05) "plutonium panda"

### added 

* [Add .proto output files to kivakit-microservice GRPC service](https://github.com/Telenav/kivakit/issues/89)

### fixed

 * [Division microservice example is broken #87](https://github.com/Telenav/kivakit/issues/87)
 * [Broken listener chain due to inheritance from mixin and base component #86](https://github.com/Telenav/kivakit/issues/86)
 * [Documentation links are broken in kivakit 1.1.0 #88](https://github.com/Telenav/kivakit/issues/88)

## Version 1.1.0 (2021.08.27) "plutonium snake"

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

## Version 1.1.1 (2021.08.27) "puffy mouse"

Initial release of KivaKit.

![](https://www.kivakit.org/images/horizontal-line.png)

