# kivakit-service registry &nbsp;&nbsp;![](https://www.kivakit.org/images/gears-40.png)

This project provides shared registry code to kivakit-service-client and kivakit-service-server.

![](https://www.kivakit.org/images/horizontal-line.png)

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://www.kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-service-registry</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](https://www.kivakit.org/images/short-horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

**This module is not public API**. It is consumed by the [client](../client/README.md) and [server](../server/README.md) modules
to provide core registration and discovery functionality.

[//]: # (end-user-text)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://www.kivakit.org/images/diagram-48.png)

[*Service Registry*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/diagram-registry.svg)  
[*Service Registry REST Protocol*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/diagram-rest.svg)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/box-40.png)

[*com.telenav.kivakit.service.registry*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.svg)  
[*com.telenav.kivakit.service.registry.project*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.project.svg)  
[*com.telenav.kivakit.service.registry.protocol*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.protocol.svg)  
[*com.telenav.kivakit.service.registry.protocol.discover*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.protocol.discover.svg)  
[*com.telenav.kivakit.service.registry.protocol.register*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.protocol.register.svg)  
[*com.telenav.kivakit.service.registry.protocol.renew*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.protocol.renew.svg)  
[*com.telenav.kivakit.service.registry.protocol.update*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.protocol.update.svg)  
[*com.telenav.kivakit.service.registry.registries*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.registries.svg)  
[*com.telenav.kivakit.service.registry.serialization*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.serialization.svg)  
[*com.telenav.kivakit.service.registry.serialization.serializers*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.serialization.serializers.svg)  
[*com.telenav.kivakit.service.registry.store*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/com.telenav.kivakit.service.registry.store.svg)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/books-40.png)

Javadoc coverage for this project is 95.5%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-100-12.png)



| Class | Documentation Sections |
|---|---|
| [*ApplicationIdentifierSerializer*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/serialization/serializers/ApplicationIdentifierSerializer.html) |  |  
| [*BaseRequest*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/BaseRequest.html) |  |  
| [*BaseResponse*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/BaseResponse.html) |  |  
| [*BaseServiceRegistry*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/registries/BaseServiceRegistry.html) |  |  
| [*DiscoverApplicationsRequest*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/discover/DiscoverApplicationsRequest.html) |  |  
| [*DiscoverApplicationsResponse*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/discover/DiscoverApplicationsResponse.html) |  |  
| [*DiscoverPortServiceRequest*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/discover/DiscoverPortServiceRequest.html) |  |  
| [*DiscoverPortServiceResponse*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/discover/DiscoverPortServiceResponse.html) |  |  
| [*DiscoverServicesRequest*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/discover/DiscoverServicesRequest.html) |  |  
| [*DiscoverServicesRequest.SearchType*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/discover/DiscoverServicesRequest.SearchType.html) |  |  
| [*DiscoverServicesResponse*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/discover/DiscoverServicesResponse.html) |  |  
| [*LocalServiceRegistry*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/registries/LocalServiceRegistry.html) |  |  
| [*NetworkRegistryUpdateRequest*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/update/NetworkRegistryUpdateRequest.html) |  |  
| [*NetworkRegistryUpdateResponse*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/update/NetworkRegistryUpdateResponse.html) |  |  
| [*NetworkServiceRegistry*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/registries/NetworkServiceRegistry.html) |  |  
| [*ProblemSerializer*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/serialization/serializers/ProblemSerializer.html) |  |  
| [*RegisterServiceRequest*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/register/RegisterServiceRequest.html) |  |  
| [*RegisterServiceResponse*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/register/RegisterServiceResponse.html) |  |  
| [*RenewServiceRequest*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/renew/RenewServiceRequest.html) |  |  
| [*RenewServiceResponse*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/renew/RenewServiceResponse.html) |  |  
| [*Scope*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/Scope.html) |  |  
| [*Scope.Type*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/Scope.Type.html) |  |  
| [*Service*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/Service.html) | Service Registration and Expiration |  
| | Service Discovery |  
| | Service Properties |  
| [*ServiceMetadata*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/ServiceMetadata.html) | Service Metadata |  
| [*ServiceRegistry*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/ServiceRegistry.html) |  |  
| [*ServiceRegistryGsonFactory*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/serialization/ServiceRegistryGsonFactory.html) |  |  
| [*ServiceRegistryJerseySerializer*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/serialization/ServiceRegistryJerseySerializer.html) |  |  
| [*ServiceRegistryKryoTypes*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/project/ServiceRegistryKryoTypes.html) |  |  
| [*ServiceRegistryProject*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/project/ServiceRegistryProject.html) |  |  
| [*ServiceRegistryProtocol*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/protocol/ServiceRegistryProtocol.html) |  |  
| [*ServiceRegistrySettings*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/ServiceRegistrySettings.html) | ServiceRegistrySettings.properties |  
| [*ServiceRegistryStore*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/store/ServiceRegistryStore.html) |  |  
| [*ServiceRegistryUpdater*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/ServiceRegistryUpdater.html) |  |  
| [*ServiceType*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/ServiceType.html) |  |  
| [*ServiceTypeSerializer*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/serialization/serializers/ServiceTypeSerializer.html) |  |  
| [*TimeSerializer*](https://www.kivakit.org/javadoc/kivakit/kivakit.service.registry/com/telenav/kivakit/service/registry/serialization/serializers/TimeSerializer.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](https://www.kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.18. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

