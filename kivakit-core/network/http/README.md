# kivakit-core-network-http &nbsp;&nbsp;![](../../../documentation/images/world-40.png)

This module provides HTTP and HTTPS resources.

![](documentation/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Get**](#get)  
[**Authentication**](#authentication)  
[**Encryption**](#encryption)  
[**Dependencies**](#dependencies)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

![](documentation/images/horizontal-line.png)

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

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp;  ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-network-http</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp;![](documentation/images/diagram-48.png)

[*HTTP*](documentation/diagrams/diagram-http.svg)  
[*Secure HTTP*](documentation/diagrams/diagram-https.svg)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.kivakit.core.network.http*](documentation/diagrams/com.telenav.kivakit.core.network.http.svg)  
[*com.telenav.kivakit.core.network.http.project*](documentation/diagrams/com.telenav.kivakit.core.network.http.project.svg)  
[*com.telenav.kivakit.core.network.http.secure*](documentation/diagrams/com.telenav.kivakit.core.network.http.secure.svg)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*
BaseHttpResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/BaseHttpResource.html) |  |  
| [*
CoreNetworkHttpProject*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/project/CoreNetworkHttpProject.html) |  |  
| [*
HttpAccessConstraints*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpAccessConstraints.html) |  |  
| [*
HttpBasicCredentials*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpBasicCredentials.html) |  |  
| [*
HttpGetResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpGetResource.html) |  |  
| [*
HttpNetworkLocation*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpNetworkLocation.html) |  |  
| [*
HttpNetworkLocation.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpNetworkLocation.Converter.html) |  |  
| [*
HttpPostResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpPostResource.html) |  |  
| [*
HttpPutResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpPutResource.html) |  |  
| [*
HttpStatus*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/HttpStatus.html) |  |  
| [*
InvalidCertificateTrustingHttpClient*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/InvalidCertificateTrustingHttpClient.html) |  |  
| [*
SecureHttpGetResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpGetResource.html) |  |  
| [*
SecureHttpNetworkLocation*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpNetworkLocation.html) |  |  
| [*
SecureHttpPostResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.network.http/com/telenav/kivakit/core/network/http/secure/SecureHttpPostResource.html) |  |  

[//]: # (start-user-text)


[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>Copyright 2011-2021 [Telenav](http://telenav.com), Inc. Licensed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by Lexakai on 2021.04.01</sub>    
<sub>UML diagrams courtesy of PlantUML (http://plantuml.com)</sub>

