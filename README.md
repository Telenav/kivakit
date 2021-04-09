![](documentation/images/kivakit-background-500.jpg)

# KivaKit 0.9.0-SNAPSHOT &nbsp;&nbsp;![](documentation/images/kivakit-64.png)

KivaKit is a set of integrated Java mini-frameworks.

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)

### Welcome <a name = "welcome"></a>! &nbsp; ![](documentation/images/stars-48.png)

> *The mission of KivaKit is to accelerate the development of Java software through the use of integrated, modular, object-oriented design.*

KivaKit provides quick and easy solutions to common everyday software problems by providing APIs, and sometimes by "wrapping" them in a
simplified abstraction. It is not KivaKit's goal to provide access to all features of underlying APIs, but the most frequently used features
are usually available or can be added.

KivaKit is composed of a number of nested sub-projects that are Java 11+ modules. These modules address different areas of concern in
developing Java applications. The resources below will help you to get started.

![](documentation/images/horizontal-line.png)

### Summary <a name = "summary"></a>

#### What is it?

- KivaKit is a set of integrated Java development mini-frameworks for common tasks
- KivaKit modules are designed to work together, producing reliable and reusable software
- KivaKit accelerates development and makes code simpler and clearer

#### What can it do?

KivaKit makes it easy to:

- **Broadcast and listen for messages**
    - Decouple status reporting from status handling
    - Connect components in listener chains
    - Report and log conditions
- **Validate** data
- **Convert** types with consistent semantics that increase reuse
- **Register and locate services**
    - Dynamically allocate service ports on the local host
    - Locate services on the local host, in a cluster or on the network
- **Log messages** to different log implementations, including:
    - Files
    - Email
    - Console
- **Access resources** in a simple, powerful and consistent way, including:
    - Files and folders
    - HDFS
    - S3
    - Java packages
    - Module and classpath resources
    - HTTP resources
    - Zip file entries
- **Serialize data** with an extensible abstraction
- **Create command-line applications**
    - Parse command line arguments in a reusable way
    - Configure components programmatically, or by loading settings   
      from properties files
- **Process and compress data**
- **Quickly run embedded [Jetty](https://www.eclipse.org/jetty/)** with [Apache Wicket](https://wicket.apache.org),
  [Jersey](https://eclipse-ee4j.github.io/jersey/) and [Swagger](https://swagger.io).

Each of these features is designed for simplicity, consistency and integration with other  
parts of the framework.

### Project Resources <a name = "project-resources"></a> &nbsp; ![](documentation/images/water-32.png)

| Resource     |     Description                   |
|--------------|-----------------------------------|
| Project Name | KivaKit |
| Summary | Library code for developing Java applications |
| Javadoc Coverage |  <!-- ${project-javadoc-average-coverage-meter} -->  ![](documentation/images/meter-80-12.png) <!-- end --> |
| Lead | Jonathan Locke (Luo, Shibo) <br/> [jonathanl@telenav.com](mailto:jonathanl@telenav.com) |
| Administrator | Jonathan Locke (Luo, Shibo) <br/> [jonathanl@telenav.com](mailto:jonathanl@telenav.com) |
| Email | [jonathanl@telenav.com](mailto:jonathanl@telenav.com) |
| Issues | [GitHub Telenav/kivakit/issues](https://github.com/Telenav/kivakit/issues) |
| Code | [GitHub Telenav/kivakit](https://github.com/Telenav/kivakit) |
| Checkout | `git clone git@github.com:Telenav/kivakit.git` |

<br/> 

### Quick Start <a name = "quick-start"></a>&nbsp; ![](documentation/images/rocket-40.png)

[**Setup**](documentation/overview/setup.md)  
[**Building**](documentation/overview/building.md)  
[**Developing**](documentation/developing/index.md)

### Downloads <a name = "downloads"></a>&nbsp; ![](documentation/images/down-arrow-32.png)

[**Java 12**](https://www.oracle.com/java/technologies/javase/jdk12-archive-downloads.html)  
[**Maven**](https://maven.apache.org/download.cgi)  
[**IntelliJ**](https://www.jetbrains.com/idea/download/)

### Reference <a name = "reference"></a>&nbsp; ![](documentation/images/books-40.png)

[**Javadoc**](https://telenav.github.io/kivakit/javadoc)  
[**CodeFlowers**](https://telenav.github.io/kivakit/codeflowers/site/index.html)  
[**System Properties**](documentation/developing/system-properties.md)

[//]: # (end-user-text)

### Projects &nbsp; ![](documentation/images/gears-40.png)

[**kivakit-core**](kivakit-core/README.md)  
[**kivakit-data**](kivakit-data/README.md)  
[**kivakit-filesystems**](kivakit-filesystems/README.md)  
[**kivakit-logs**](kivakit-logs/README.md)  
[**kivakit-service**](kivakit-service/README.md)  
[**kivakit-web**](kivakit-web/README.md)  

### Javadoc Coverage

&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-application*  
&nbsp;  ![](documentation/images/meter-60-12.png) &nbsp; &nbsp; *kivakit-core-collections*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-commandline*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-configuration*  
&nbsp;  ![](documentation/images/meter-40-12.png) &nbsp; &nbsp; *kivakit-core-kernel*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-network-core*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-network-email*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-network-ftp*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-network-http*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-network-socket*  
&nbsp;  ![](documentation/images/meter-30-12.png) &nbsp; &nbsp; *kivakit-core-resource*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-security*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-serialization-core*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-serialization-jersey-json*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-serialization-json*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-serialization-kryo*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-core-test*  
&nbsp;  ![](documentation/images/meter-70-12.png) &nbsp; &nbsp; *kivakit-data-compression*  
&nbsp;  ![](documentation/images/meter-40-12.png) &nbsp; &nbsp; *kivakit-data-formats-csv*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-data-formats-library*  
&nbsp;  ![](documentation/images/meter-0-12.png) &nbsp; &nbsp; *kivakit-data-formats-text*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-filesystems-hdfs*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-filesystems-hdfs-proxy*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-filesystems-hdfs-proxy-spi*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-filesystems-s3fs*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-logs-email*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-logs-file*  
&nbsp;  ![](documentation/images/meter-30-12.png) &nbsp; &nbsp; *kivakit-service-client*  
&nbsp;  ![](documentation/images/meter-20-12.png) &nbsp; &nbsp; *kivakit-service-registry*  
&nbsp;  ![](documentation/images/meter-0-12.png) &nbsp; &nbsp; *kivakit-service-server*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-web-jersey*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-web-jetty*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-web-swagger*  
&nbsp;  ![](documentation/images/meter-100-12.png) &nbsp; &nbsp; *kivakit-web-wicket*

[//]: # (start-user-text)

![](documentation/images/horizontal-line.png)

[**Issues**](https://github.com/Telenav/kivakit/issues) |
[**Change Log**](change-log.md) |
[**Java Migration Notes**](documentation/overview/java-migration-notes.md)

[//]: # (end-user-text)

![](documentation/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.08. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>
