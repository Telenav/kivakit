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

# kivakit-network core &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons//nucleus-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons//nucleus-32-2x.png 2x"/>

This module provides classes that enhance the core networking features of the JDK. Other projects in  
kivakit-core-network extend this base functionality.

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Hosts**](#hosts)  
[**Network Resources**](#network-resources)  
[**Email Addresses**](#email-addresses)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.6.0/lexakai/kivakit/kivakit-network/core/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-network-core</artifactId>
        <version>1.6.0</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

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

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*Hosts, Ports and Protocols*](https://www.kivakit.org/1.6.0/lexakai/kivakit/kivakit-network/core/documentation/diagrams/diagram-port.svg)  
[*Network Locations*](https://www.kivakit.org/1.6.0/lexakai/kivakit/kivakit-network/core/documentation/diagrams/diagram-network-location.svg)  
[*diagram-authentication*](https://www.kivakit.org/1.6.0/lexakai/kivakit/kivakit-network/core/documentation/diagrams/diagram-authentication.svg)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-32-2x.png 2x"/>

[*com.telenav.kivakit.network.core*](https://www.kivakit.org/1.6.0/lexakai/kivakit/kivakit-network/core/documentation/diagrams/com.telenav.kivakit.network.core.svg)  
[*com.telenav.kivakit.network.core.authentication*](https://www.kivakit.org/1.6.0/lexakai/kivakit/kivakit-network/core/documentation/diagrams/com.telenav.kivakit.network.core.authentication.svg)  
[*com.telenav.kivakit.network.core.authentication.passwords*](https://www.kivakit.org/1.6.0/lexakai/kivakit/kivakit-network/core/documentation/diagrams/com.telenav.kivakit.network.core.authentication.passwords.svg)  
[*com.telenav.kivakit.network.core.lexakai*](https://www.kivakit.org/1.6.0/lexakai/kivakit/kivakit-network/core/documentation/diagrams/com.telenav.kivakit.network.core.lexakai.svg)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

Javadoc coverage for this project is 83.3%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/meter-80-96.png" srcset="https://telenav.github.io/telenav-assets/meter-80-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*BaseNetworkResource*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////////////.html) |  |  
| [*DiagramAuthentication*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core///////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramNetworkLocation*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core////////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramPort*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////////////.html) |  |  
| [*DigestPassword*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////////////////////////////////.html) |  |  
| [*EmailAddress*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core//////////////////////////////////////////////.html) |  |  
| [*EmailAddress.Converter*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core////////////////////////////////////////////////////////.html) |  |  
| [*Host*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core//////////////////////////////////////.html) | Attributes |  
| | Ports |  
| [*Host.Converter*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core////////////////////////////////////////////////.html) |  |  
| [*LocalHost*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core///////////////////////////////////////////.html) |  |  
| [*Loopback*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core//////////////////////////////////////////.html) |  |  
| [*NetworkAccessConstraints*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core//////////////////////////////////////////////////////////.html) |  |  
| [*NetworkLocation*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////////.html) |  |  
| [*NetworkLocation.Converter*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core///////////////////////////////////////////////////////////.html) |  |  
| [*NetworkPath*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////.html) | Path Parsing Methods |  
| | Path Factory Methods |  
| [*NetworkPath.Converter*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core///////////////////////////////////////////////////////.html) |  |  
| [*NetworkResource*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////////.html) |  |  
| [*Password*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////////////////.html) |  |  
| [*PlainTextPassword*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*PlainTextPassword.Converter*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core//////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*Port*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core//////////////////////////////////////.html) |  |  
| [*Port.Converter*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core////////////////////////////////////////////////.html) |  |  
| [*Protocol*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core//////////////////////////////////////////.html) |  |  
| [*QueryParameters*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////////.html) |  |  
| [*UserName*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core/////////////////////////////////////////////////////////.html) |  |  
| [*UserName.Converter*](https://www.kivakit.org/1.6.0/javadoc/kivakit/kivakit.network.core///////////////////////////////////////////////////////////////////.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
