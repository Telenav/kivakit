# kivakit-core-network http &nbsp;&nbsp;![](https://kivakit.org/images/world-40.png)

This module provides HTTP and HTTPS resources.

![](https://kivakit.org/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Get**](#get)  
[**Authentication**](#authentication)  
[**Encryption**](#encryption)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId></artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](https://kivakit.org/images/short-horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module supports HTTP get, post and put operations, both over HTTP and HTTPS protocols. Basic  
access credentials are supported and *HttpNetworkLocation* provides convenient methods for accessing  
*BaseHttpResource*s.

### Get <a name = "get"></a>, Post and Put

The most fundamental HTTP operations, GET, POST and PUT are provided by the classes *HttpGetResource*,  
*HttpPostResource* and *HttpPutResource*. As *Resource* and *WritableResource* subclasses, these classes  
inherit all resource functionality from the [*kivakit-core-resource*](../../resource/README.md) mini-framework. This makes code accepting  
*Resource* parameters more general.

### Authentication <a name = "authentication"></a>

The *HttpAccessConstraints* class adds *HttpBasicCredentials* support to its *NetworkAccessConstraints* superclass.

### Encryption <a name = "encryption"></a>

The *SecureHttpNetworkLocation*, *SecureHttpGetResource* and *SecureHttpPostResource* classes add HTTPS support.

[//]: # (end-user-text)

![](https://kivakit.org/images/short-horizontal-line.png)

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://kivakit.org/images/diagram-48.png)

[*HTTP*](documentation/diagrams/diagram-http.svg)  
[*Secure HTTP*](documentation/diagrams/diagram-https.svg)  

![](https://kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/box-40.png)

[*com.telenav.kivakit.core.network.http*](documentation/diagrams/com.telenav.kivakit.core.network.http.svg)  
[*com.telenav.kivakit.core.network.http.project*](documentation/diagrams/com.telenav.kivakit.core.network.http.project.svg)  
[*com.telenav.kivakit.core.network.http.secure*](documentation/diagrams/com.telenav.kivakit.core.network.http.secure.svg)  

![](https://kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/books-40.png)

Javadoc coverage for this project is 90.4%.  
  
&nbsp; &nbsp;  ![](https://kivakit.org/images/meter-90-12.png)



| Class | Documentation Sections |
|---|---|
| [*BaseHttpResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/BaseHttpResource.html) | Content |  
| [*CoreNetworkHttpProject*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/project/CoreNetworkHttpProject.html) |  |  
| [*HttpAccessConstraints*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpAccessConstraints.html) |  |  
| [*HttpBasicCredentials*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpBasicCredentials.html) |  |  
| [*HttpGetResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpGetResource.html) |  |  
| [*HttpNetworkLocation*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpNetworkLocation.html) | Access |  
| [*HttpNetworkLocation.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpNetworkLocation.Converter.html) |  |  
| [*HttpPostResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpPostResource.html) |  |  
| [*HttpPutResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpPutResource.html) |  |  
| [*HttpStatus*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpStatus.html) |  |  
| [*InvalidCertificateTrustingHttpClient*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/InvalidCertificateTrustingHttpClient.html) |  |  
| [*SecureHttpGetResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpGetResource.html) |  |  
| [*SecureHttpNetworkLocation*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpNetworkLocation.html) |  |  
| [*SecureHttpPostResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpPostResource.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](https://kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.15. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

