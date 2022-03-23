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

# kivakit-network core &nbsp;&nbsp; <img src="https://www.kivakit.org/images/nucleus-32.png" srcset="https://www.kivakit.org/images/nucleus-32-2x.png 2x"/>

This module provides classes that enhance the core networking features of the JDK. Other projects in  
kivakit-core-network extend this base functionality.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Hosts**](#hosts)  
[**Network Resources**](#network-resources)  
[**Email Addresses**](#email-addresses)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.4.1/lexakai/kivakit/kivakit-network/core/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-network-core</artifactId>
        <version>1.4.1</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module provides classes for working with networks, including hosts, ports, protocols,
network resources and email addresses.

### Hosts <a name = "hosts"></a>, Ports and Protocols

The *Port* class represents a port *on a particular host*. This helps reduce clutter by not requiring
that a *Host* object be passed along with an integer port number (as is often the case in everyday
code). It also associates the *Port* with a *Protocol*. By simply passing in a *Port*, all information
required to use the port is made available: the host, port number and protocol.

The *Host* class provides a number of methods for constructing common ports. For example:

    var port = host.http(8080);

The *LocalHost* and *Loopback* (127.0.0.1) hosts can be retrieved with *Host.local()* and *Host.loopback()*.

### Network Resources <a name = "network-resources"></a>

The *NetworkResource* interface integrates various types of network resources with the [*resource*](../../resource/README.md)
mini-framework. This interface looks like this:

    public interface NetworkResource extends Resource
    {
        NetworkLocation location();
    }

A *NetworkLocation* combines all the properties required to access a network resource:

* The *Port* (including *Host* and *Protocol*) where the resource is located
* The *NetworkPath* on the host port where the resource can be requested
* Any *NetworkConstraints* for accessing the resource, such as authentication, timeout or
  frequency of access.
* Any *QueryParameters* attached to the URI/URL of the resource.

To appreciate how powerful all this abstraction can be, consider the problem of retrieving
and running a JAR file from an arbitrary web location. The *HttpJarLauncher* achieves this
with a minimum of code:

    HttpNetworkLocation source;
    File jar;

    [...]

    jar.parent().mkdirs();
    jar.safeCopyFrom(source.get(), CopyMode.OVERWRITE, progress);

This copies the *Resource* retrieved from the *HttpNetworkLocation* with *get()* (which will be
an *HttpGetResource*), to the *File* *jar*. Of course, this could be made even more general with
a little more effort by changing *HttpNetworkLocation* to *NetworkLocation*.

The *BaseNetworkResource* provides a base class from which various network resource types,
such as *HttpGetResource*, can descend.

### Email Addresses <a name = "email-addresses"></a>

The *EmailAddress* class parses and represents email addresses according to RFC-5322. KivaKit
project [*kivakit-network-core-email*](../email/README.md) makes use of this class in composing and sending emails.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Hosts, Ports and Protocols*](https://www.kivakit.org/1.4.1/lexakai/kivakit/kivakit-network/core/documentation/diagrams/diagram-port.svg)  
[*Network Locations*](https://www.kivakit.org/1.4.1/lexakai/kivakit/kivakit-network/core/documentation/diagrams/diagram-network-location.svg)  
[*diagram-authentication*](https://www.kivakit.org/1.4.1/lexakai/kivakit/kivakit-network/core/documentation/diagrams/diagram-authentication.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.network.core*](https://www.kivakit.org/1.4.1/lexakai/kivakit/kivakit-network/core/documentation/diagrams/com.telenav.kivakit.network.core.svg)  
[*com.telenav.kivakit.network.core.authentication*](https://www.kivakit.org/1.4.1/lexakai/kivakit/kivakit-network/core/documentation/diagrams/com.telenav.kivakit.network.core.authentication.svg)  
[*com.telenav.kivakit.network.core.authentication.passwords*](https://www.kivakit.org/1.4.1/lexakai/kivakit/kivakit-network/core/documentation/diagrams/com.telenav.kivakit.network.core.authentication.passwords.svg)  
[*com.telenav.kivakit.network.core.lexakai*](https://www.kivakit.org/1.4.1/lexakai/kivakit/kivakit-network/core/documentation/diagrams/com.telenav.kivakit.network.core.lexakai.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 83.3%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-80-96.png" srcset="https://www.kivakit.org/images/meter-80-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*BaseNetworkResource*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/BaseNetworkResource.html) |  |  
| [*DiagramAuthentication*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/lexakai/DiagramAuthentication.html) |  |  
| [*DiagramNetworkLocation*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/lexakai/DiagramNetworkLocation.html) |  |  
| [*DiagramPort*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/lexakai/DiagramPort.html) |  |  
| [*DigestPassword*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/authentication/passwords/DigestPassword.html) |  |  
| [*EmailAddress*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/EmailAddress.html) |  |  
| [*EmailAddress.Converter*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/EmailAddress.Converter.html) |  |  
| [*Host*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/Host.html) | Attributes |  
| | Ports |  
| [*Host.Converter*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/Host.Converter.html) |  |  
| [*LocalHost*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/LocalHost.html) |  |  
| [*Loopback*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/Loopback.html) |  |  
| [*NetworkAccessConstraints*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/NetworkAccessConstraints.html) |  |  
| [*NetworkLocation*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/NetworkLocation.html) |  |  
| [*NetworkLocation.Converter*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/NetworkLocation.Converter.html) |  |  
| [*NetworkPath*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/NetworkPath.html) | Path Parsing Methods |  
| | Path Factory Methods |  
| [*NetworkPath.Converter*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/NetworkPath.Converter.html) |  |  
| [*NetworkResource*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/NetworkResource.html) |  |  
| [*Password*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/authentication/Password.html) |  |  
| [*PlainTextPassword*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/authentication/passwords/PlainTextPassword.html) |  |  
| [*PlainTextPassword.Converter*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/authentication/passwords/PlainTextPassword.Converter.html) |  |  
| [*Port*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/Port.html) |  |  
| [*Port.Converter*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/Port.Converter.html) |  |  
| [*Protocol*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/Protocol.html) |  |  
| [*QueryParameters*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/QueryParameters.html) |  |  
| [*UserName*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/authentication/UserName.html) |  |  
| [*UserName.Converter*](https://www.kivakit.org/1.4.1/javadoc/kivakit/kivakit.network.core/com/telenav/kivakit/network/core/authentication/UserName.Converter.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

