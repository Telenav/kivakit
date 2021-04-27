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

# kivakit-core-network http &nbsp;&nbsp; <img src="https://www.kivakit.org/images/world-32.png" srcset="https://www.kivakit.org/images/world-32-2x.png 2x"/>

This module provides HTTP and HTTPS resources.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Get**](#get)  
[**Authentication**](#authentication)  
[**Encryption**](#encryption)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/network/http/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-network-http</artifactId>
        <version>0.9.5-alpha-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module supports HTTP get, post and put operations, both over HTTP and HTTPS protocols. Basic
access credentials are supported and *HttpNetworkLocation* provides convenient methods for accessing
*BaseHttpResource*s.

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Get <a name = "get"></a>, Post and Put

The most fundamental HTTP operations, GET, POST and PUT are provided by the classes *HttpGetResource*,
*HttpPostResource* and *HttpPutResource*. As *Resource* and *WritableResource* subclasses, these classes
inherit all resource functionality from the [*kivakit-core-resource*](../../resource/README.md) mini-framework. This makes code accepting
*Resource* parameters more general.

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Authentication <a name = "authentication"></a>

The *HttpAccessConstraints* class adds *HttpBasicCredentials* support to its *NetworkAccessConstraints* superclass.

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Encryption <a name = "encryption"></a>

The *SecureHttpNetworkLocation*, *SecureHttpGetResource* and *SecureHttpPostResource* classes add HTTPS support.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*HTTP*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/network/http/documentation/diagrams/diagram-http.svg)  
[*Secure HTTP*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/network/http/documentation/diagrams/diagram-https.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.core.network.http*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/network/http/documentation/diagrams/com.telenav.kivakit.core.network.http.svg)  
[*com.telenav.kivakit.core.network.http.project*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/network/http/documentation/diagrams/com.telenav.kivakit.core.network.http.project.svg)  
[*com.telenav.kivakit.core.network.http.secure*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/network/http/documentation/diagrams/com.telenav.kivakit.core.network.http.secure.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 90.4%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-90-96.png" srcset="https://www.kivakit.org/images/meter-90-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*BaseHttpResource*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/BaseHttpResource.html) | Content |  
| [*CoreNetworkHttpProject*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/project/CoreNetworkHttpProject.html) |  |  
| [*HttpAccessConstraints*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpAccessConstraints.html) |  |  
| [*HttpBasicCredentials*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpBasicCredentials.html) |  |  
| [*HttpGetResource*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpGetResource.html) |  |  
| [*HttpNetworkLocation*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpNetworkLocation.html) | Access |  
| [*HttpNetworkLocation.Converter*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpNetworkLocation.Converter.html) |  |  
| [*HttpPostResource*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpPostResource.html) |  |  
| [*HttpPutResource*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpPutResource.html) |  |  
| [*HttpStatus*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpStatus.html) |  |  
| [*InvalidCertificateTrustingHttpClient*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/InvalidCertificateTrustingHttpClient.html) |  |  
| [*SecureHttpGetResource*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpGetResource.html) |  |  
| [*SecureHttpNetworkLocation*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpNetworkLocation.html) |  |  
| [*SecureHttpPostResource*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpPostResource.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

