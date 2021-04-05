# kivakit-service-registry &nbsp;&nbsp;![](../../documentation/images/log-40.png)

This project provides shared registry code to kivakit-service-client and kivakit-service-server.

![](documentation/images/horizontal-line.png)

### Index

[**Dependencies**](#dependencies)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)


[//]: # (end-user-text)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp;  ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-service-registry</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp;![](documentation/images/diagram-48.png)

[*Service Registry*](documentation/diagrams/diagram-registry.svg)  
[*Service Registry REST Protocol*](documentation/diagrams/diagram-rest.svg)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.tdk.service.registry*](documentation/diagrams/com.telenav.tdk.service.registry.svg)  
[*com.telenav.tdk.service.registry.project*](documentation/diagrams/com.telenav.tdk.service.registry.project.svg)  
[*
com.telenav.tdk.service.registry.project.lexakai.annotations*](documentation/diagrams/com.telenav.tdk.service.registry.project.lexakai.annotations.svg)  
[*com.telenav.tdk.service.registry.protocol*](documentation/diagrams/com.telenav.tdk.service.registry.protocol.svg)  
[*com.telenav.tdk.service.registry.protocol.discover*](documentation/diagrams/com.telenav.tdk.service.registry.protocol.discover.svg)  
[*com.telenav.tdk.service.registry.protocol.register*](documentation/diagrams/com.telenav.tdk.service.registry.protocol.register.svg)  
[*com.telenav.tdk.service.registry.protocol.renew*](documentation/diagrams/com.telenav.tdk.service.registry.protocol.renew.svg)  
[*com.telenav.tdk.service.registry.protocol.update*](documentation/diagrams/com.telenav.tdk.service.registry.protocol.update.svg)  
[*com.telenav.tdk.service.registry.registries*](documentation/diagrams/com.telenav.tdk.service.registry.registries.svg)  
[*com.telenav.tdk.service.registry.serialization*](documentation/diagrams/com.telenav.tdk.service.registry.serialization.svg)  
[*
com.telenav.tdk.service.registry.serialization.serializers*](documentation/diagrams/com.telenav.tdk.service.registry.serialization.serializers.svg)  
[*com.telenav.tdk.service.registry.store*](documentation/diagrams/com.telenav.tdk.service.registry.store.svg)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*
ApplicationIdentifierSerializer*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/serialization/serializers/ApplicationIdentifierSerializer.html) |  |  
| [*
BaseRequest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/BaseRequest.html) |  |  
| [*
BaseResponse*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/BaseResponse.html) |  |  
| [*
BaseServiceRegistry*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/registries/BaseServiceRegistry.html) |  |  
| [*
DiagramRegistry*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/project/lexakai/annotations/DiagramRegistry.html) |  |  
| [*
DiagramRest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/project/lexakai/annotations/DiagramRest.html) |  |  
| [*
DiscoverApplicationsRequest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/discover/DiscoverApplicationsRequest.html) |  |  
| [*
DiscoverApplicationsResponse*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/discover/DiscoverApplicationsResponse.html) |  |  
| [*
DiscoverPortServiceRequest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/discover/DiscoverPortServiceRequest.html) |  |  
| [*
DiscoverPortServiceResponse*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/discover/DiscoverPortServiceResponse.html) |  |  
| [*
DiscoverServicesRequest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/discover/DiscoverServicesRequest.html) |  |  
| [*
DiscoverServicesRequest.SearchType*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/discover/DiscoverServicesRequest.SearchType.html) |  |  
| [*
DiscoverServicesResponse*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/discover/DiscoverServicesResponse.html) |  |  
| [*
LocalServiceRegistry*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/registries/LocalServiceRegistry.html) |  |  
| [*
NetworkRegistryUpdateRequest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/update/NetworkRegistryUpdateRequest.html) |  |  
| [*
NetworkRegistryUpdateResponse*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/update/NetworkRegistryUpdateResponse.html) |  |  
| [*
NetworkServiceRegistry*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/registries/NetworkServiceRegistry.html) |  |  
| [*
ProblemSerializer*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/serialization/serializers/ProblemSerializer.html) |  |  
| [*
RegisterServiceRequest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/register/RegisterServiceRequest.html) |  |  
| [*
RegisterServiceResponse*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/register/RegisterServiceResponse.html) |  |  
| [*
RenewServiceRequest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/renew/RenewServiceRequest.html) |  |  
| [*
RenewServiceResponse*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/renew/RenewServiceResponse.html) |  |  
| [*
Scope*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/Scope.html) |  |  
| [*
Scope.Type*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/Scope.Type.html) |  |  
| [*
Service*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/Service.html) | Service Registration and Expiration |  
| | Service Discovery |  
| | Service Properties |  
| [*
ServiceMetadata*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/ServiceMetadata.html) | Service Metadata |  
| [*
ServiceRegistry*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/ServiceRegistry.html) |  |  
| [*
ServiceRegistryGsonFactory*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/serialization/ServiceRegistryGsonFactory.html) |  |  
| [*
ServiceRegistryJerseySerializer*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/serialization/ServiceRegistryJerseySerializer.html) |  |  
| [*
ServiceRegistryKryoTypes*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/project/ServiceRegistryKryoTypes.html) |  |  
| [*
ServiceRegistryProject*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/project/ServiceRegistryProject.html) |  |  
| [*
ServiceRegistryProtocol*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/protocol/ServiceRegistryProtocol.html) |  |  
| [*
ServiceRegistrySettings*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/ServiceRegistrySettings.html) |  |  
| [*
ServiceRegistryStore*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/store/ServiceRegistryStore.html) |  |  
| [*
ServiceRegistryUpdater*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/ServiceRegistryUpdater.html) |  |  
| [*
ServiceType*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/ServiceType.html) |  |  
| [*
ServiceTypeSerializer*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/serialization/serializers/ServiceTypeSerializer.html) |  |  
| [*
TimeSerializer*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.tdk.service.registry/com/telenav/tdk/service/registry/serialization/serializers/TimeSerializer.html) |  |  

[//]: # (start-user-text)


[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>This documentation was generated by Lexakai on 2021.04.01</sub>    
<sub>UML diagrams courtesy of PlantUML (http://plantuml.com), distributed unmodified under Apache License, Version 2.0.</sub>

