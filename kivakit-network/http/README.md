[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://telenav.github.io/telenav-assets/images/icons/web-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-network http &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/world-64.png" srcset="https://telenav.github.io/telenav-assets/images/icons/world-64-2x.png 2x"/>

This module provides HTTP and HTTPS resources.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Get**](#get)  
[**Authentication**](#authentication)  
[**Encryption**](#encryption)  

[**Dependencies**](#dependencies) | [**Code Quality**](#code-quality) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.17.1/lexakai/kivakit/kivakit-network/http/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-network-http</artifactId>
        <version>1.17.1</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module supports HTTP get, post and put operations, both over HTTP and HTTPS protocols. Basic
access credentials are supported and *HttpNetworkLocation* provides convenient methods for accessing
*BaseHttpResource*s.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Get <a name = "get"></a>, Post and Put

The most fundamental HTTP operations, GET, POST and PUT are provided by the classes *HttpGetResource*,
*HttpPostResource* and *HttpPutResource*. As *Resource* and *WritableResource* subclasses, these classes
inherit all resource functionality from the [*kivakit-core-resource*](../../resource/README.md) mini-framework. This makes code accepting
*Resource* parameters more general.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Authentication <a name = "authentication"></a>

The *HttpAccessConstraints* class adds *HttpBasicCredentials* support to its *NetworkAccessConstraints* superclass.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Encryption <a name = "encryption"></a>

The *SecureHttpNetworkLocation*, *SecureHttpGetResource* and *SecureHttpPostResource* classes add HTTPS support.

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Code Quality <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/ruler-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/ruler-32-2x.png 2x"/>

Code quality for this project is 0.0%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>

| Measurement   | Value                    |
|---------------|--------------------------|
| Stability     | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>     |
| Testing       | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>       |
| Documentation | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/> |

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*HTTP*](https://www.kivakit.org/1.17.1/lexakai/kivakit/kivakit-network/http/documentation/diagrams/diagram-http.svg)  
[*Secure HTTP*](https://www.kivakit.org/1.17.1/lexakai/kivakit/kivakit-network/http/documentation/diagrams/diagram-https.svg)  
[*diagram-resource-service*](https://www.kivakit.org/1.17.1/lexakai/kivakit/kivakit-network/http/documentation/diagrams/diagram-resource-service.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.network.http*](https://www.kivakit.org/1.17.1/lexakai/kivakit/kivakit-network/http/documentation/diagrams/com.telenav.kivakit.network.http.svg)  
[*com.telenav.kivakit.network.http.internal.lexakai*](https://www.kivakit.org/1.17.1/lexakai/kivakit/kivakit-network/http/documentation/diagrams/com.telenav.kivakit.network.http.internal.lexakai.svg)  
[*com.telenav.kivakit.network.https*](https://www.kivakit.org/1.17.1/lexakai/kivakit/kivakit-network/http/documentation/diagrams/com.telenav.kivakit.network.https.svg)

### Javadoc <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

| Class | Documentation Sections  |
|-------|-------------------------|
| [*BaseHttpResource*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/BaseHttpResource.html) | Conversions |  
| | Content |  
| | Headers |  
| | Properties |  
| [*DiagramHttp*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/internal/lexakai/DiagramHttp.html) |  |  
| [*DiagramHttps*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/internal/lexakai/DiagramHttps.html) |  |  
| [*HttpAccessConstraints*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpAccessConstraints.html) |  |  
| [*HttpBasicCredentials*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpBasicCredentials.html) |  |  
| [*HttpGetResource*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpGetResource.html) |  |  
| [*HttpGetResourceResolver*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpGetResourceResolver.html) |  |  
| [*HttpMethod*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpMethod.html) |  |  
| [*HttpNetworkLocation*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpNetworkLocation.html) | Functional |  
| | Access |  
| [*HttpNetworkLocationConverter*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpNetworkLocationConverter.html) |  |  
| [*HttpPostResource*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpPostResource.html) |  |  
| [*HttpPutResource*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpPutResource.html) |  |  
| [*HttpRequestFactory*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpRequestFactory.html) |  |  
| [*HttpResourceFolder*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpResourceFolder.html) |  |  
| [*HttpStatus*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/http/HttpStatus.html) | Tests |  
| [*HttpsGetResource*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/https/HttpsGetResource.html) |  |  
| [*HttpsNetworkLocation*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/https/HttpsNetworkLocation.html) |  |  
| [*HttpsNetworkLocationConverter*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/https/HttpsNetworkLocationConverter.html) |  |  
| [*HttpsPostResource*](https://www.kivakit.org/1.17.1/javadoc/kivakit/kivakit-network-http/com/telenav/kivakit/network/https/HttpsPostResource.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
