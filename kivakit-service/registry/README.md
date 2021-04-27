[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://www.kivakit.org/images/web-32.png" srcset="https://www.kivakit.org/images/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://www.kivakit.org/images/twitter-32.png" srcset="https://www.kivakit.org/images/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://www.kivakit.org/images/zulip-32.png" srcset="https://www.kivakit.org/images/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-service registry &nbsp;&nbsp; <img src="https://www.kivakit.org/images/log-32.png" srcset="https://www.kivakit.org/images/log-32-2x.png 2x"/>

This project provides shared registry code to kivakit-service-client and kivakit-service-server.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-service-registry</artifactId>
        <version>0.9.5-alpha-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

**This module is not public API**. It is consumed by the [client](../client/README.md) and [server](../server/README.md) modules
to provide core registration and discovery functionality.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Service Registry*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/diagram-registry.svg)  
[*Service Registry REST Protocol*](https://www.kivakit.org/lexakai/kivakit/kivakit-service/registry/documentation/diagrams/diagram-rest.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

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

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 95.5%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-100-96.png" srcset="https://www.kivakit.org/images/meter-100-96-2x.png 2x"/>




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

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

