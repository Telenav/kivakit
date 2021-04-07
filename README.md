# KivaKit 0.9.0-SNAPSHOT &nbsp;&nbsp;![](documentation/images/gears-40.png)

KivaKit is a set of integrated Java mini-frameworks.

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)

### Welcome <a name = "welcome"></a>! &nbsp; ![](documentation/images/stars-48.png)

*The mission of KivaKit is to accelerate the development of Java software through the use of integrated, modular, object-oriented design.*

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

- **Broadcast and listen for messages** in a consistent way to:
    - Connect components in listener chains
    - Report and log operations
- **Validate** objects and data
- **Convert** types with consistent semantics in a reusable way
- **Register and locate services** by scope:
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
- **Serialize data** with an extensible abstraction using:
    - [Kryo](https://github.com/EsotericSoftware/kryo)
    - More serializers to come
- **Create command-line applications** that:
    - Parse command line arguments in a reusable way
    - Configure components programmatically, or by loading settings   
      from properties files
- **Process data**:
    - Read data in CSV format as objects with a schema model
    - Read text files as objects
    - Extract objects from data sources
    - Read XML streams
    - Compress data with an abstract [Huffman](https://en.wikipedia.org/wiki/Huffman_coding) codec
- **Run embedded [Jetty](https://www.eclipse.org/jetty/)** with [Apache Wicket](https://wicket.apache.org),
  [Jersey](https://eclipse-ee4j.github.io/jersey/) and [Swagger](https://swagger.io).

Each of these features is designed for simplicity, consistency and integration with other  
parts of the framework.

### Project Resources <a name = "project-resources"></a> &nbsp; ![](documentation/images/water-32.png)

| Resource     |     Description                   |
|--------------|-----------------------------------|
| Project Name | KivaKit |
| Summary | Library code for developing Java applications |
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

[//]: # (start-user-text)

### Downloads <a name = "downloads"></a>&nbsp; ![](documentation/images/down-arrow-32.png)

[**Java 12**](https://www.oracle.com/java/technologies/javase/jdk12-archive-downloads.html)  
[**Maven**](https://maven.apache.org/download.cgi)  
[**IntelliJ**](https://www.jetbrains.com/idea/download/)

<br/>

![](documentation/images/horizontal-line.png)

[**Issues**](https://github.com/Telenav/kivakit/issues) |
[**Change Log**](change-log.md) |
[**Java Migration Notes**](documentation/overview/java-migration-notes.md)

[//]: # (end-user-text)

![](documentation/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.06. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>
