# kivakit-core-network core &nbsp;&nbsp;![](../../../documentation/images/nucleus-40.png)

This module provides classes that enhance the core networking features of the JDK. Other projects in  
kivakit-core-network extend this base functionality.

![](documentation/images/horizontal-line.png)

### Index

[**Dependencies**](#dependencies)  
[**Summary**](#summary)  
[**Hosts**](#hosts)  
[**Network Resources**](#network-resources)  
[**Email Addresses**](#email-addresses)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

### Dependencies &nbsp;&nbsp; ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

#### Maven Dependency  

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-network-core</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](documentation/images/horizontal-line.png)

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

### Class Diagrams &nbsp; &nbsp;![](documentation/images/diagram-48.png)

[*Hosts, Ports and Protocols*](documentation/diagrams/diagram-port.svg)  
[*Network Locations*](documentation/diagrams/diagram-network-location.svg)  

### Package Diagrams &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.kivakit.core.network.core*](documentation/diagrams/com.telenav.kivakit.core.network.core.svg)  
[*com.telenav.kivakit.core.network.core.cluster*](documentation/diagrams/com.telenav.kivakit.core.network.core.cluster.svg)  
[*com.telenav.kivakit.core.network.core.project*](documentation/diagrams/com.telenav.kivakit.core.network.core.project.svg)  

### Javadoc &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*BaseNetworkResource*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/BaseNetworkResource.html) |  |  
| [*ClusterIdentifier*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/cluster/ClusterIdentifier.html) |  |  
| [*CoreNetworkCoreProject*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/project/CoreNetworkCoreProject.html) |  |  
| [*EmailAddress*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/EmailAddress.html) |  |  
| [*Host*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/Host.html) |  |  
| [*Host.Converter*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/Host.Converter.html) |  |  
| [*LocalHost*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/LocalHost.html) |  |  
| [*Loopback*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/Loopback.html) |  |  
| [*NetworkAccessConstraints*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/NetworkAccessConstraints.html) |  |  
| [*NetworkLocation*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/NetworkLocation.html) |  |  
| [*NetworkLocation.Converter*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/NetworkLocation.Converter.html) |  |  
| [*NetworkPath*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/NetworkPath.html) | Path Parsing Methods |  
| | Path Factory Methods |  
| [*NetworkPath.Converter*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/NetworkPath.Converter.html) |  |  
| [*NetworkResource*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/NetworkResource.html) |  |  
| [*Port*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/Port.html) |  |  
| [*Port.Converter*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/Port.Converter.html) |  |  
| [*Port.ListConverter*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/Port.ListConverter.html) |  |  
| [*Protocol*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/Protocol.html) |  |  
| [*QueryParameters*](https://telenav.github.io/kivakit/javadoc/kivakit.core.network.core/com/telenav/kivakit/core/network/core/QueryParameters.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>Copyright 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.04</sub>    
<sub>UML diagrams courtesy of [PlantUML](http://plantuml.com)</sub>

