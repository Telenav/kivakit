# kivakit-core commandline &nbsp;&nbsp;![](https://www.kivakit.org/images/command-line-40.png)

This project facilitates object-oriented parsing of command line arguments and switches.

![](https://www.kivakit.org/images/horizontal-line.png)

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://www.kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId></artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](https://www.kivakit.org/images/short-horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

The *kivakit-core commandline* project is used by the [kivakit-core-application](../application/README.md) project to provide easy  
command line parsing to KivaKit applications. A detailed example on how to parse command line  
arguments and switches is available in the Javadoc for the [*Application*](https://telenav.github.io/kivakit/javadoc/kivakit.core.application/com/telenav/kivakit/core/application/Application.html) class.

[//]: # (end-user-text)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://www.kivakit.org/images/diagram-48.png)

[*Argument Parsing*](documentation/diagrams/diagram-argument.svg)  
[*Command Line*](documentation/diagrams/diagram-command-line.svg)  
[*Command Line Validation*](documentation/diagrams/diagram-validation.svg)  
[*Switch Parsing*](documentation/diagrams/diagram-switch.svg)  

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/box-40.png)

[*com.telenav.kivakit.core.commandline*](documentation/diagrams/com.telenav.kivakit.core.commandline.svg)  
[*com.telenav.kivakit.core.commandline.parsing*](documentation/diagrams/com.telenav.kivakit.core.commandline.parsing.svg)  
[*com.telenav.kivakit.core.commandline.project*](documentation/diagrams/com.telenav.kivakit.core.commandline.project.svg)  

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/books-40.png)

Javadoc coverage for this project is 91.4%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-90-12.png)



| Class | Documentation Sections |
|---|---|
| [*ApplicationMetadata*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ApplicationMetadata.html) |  |  
| [*Argument*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Argument.html) |  |  
| [*ArgumentList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentList.html) |  |  
| [*ArgumentParser*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*ArgumentParser.Builder*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentParser.Builder.html) |  |  
| [*ArgumentParserList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/ArgumentParserList.html) |  |  
| [*ArgumentValidator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/ArgumentValidator.html) |  |  
| [*CommandLine*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/CommandLine.html) | Retrieving Switches and Arguments |  
| | Error Handling |  
| [*CommandLineParser*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/CommandLineParser.html) | Example |  
| | Parsing a Command Line |  
| | Switch Conventions |  
| [*CoreCommandLineProject*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/project/CoreCommandLineProject.html) |  |  
| [*Quantifier*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Quantifier.html) |  |  
| [*Switch*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Switch.html) |  |  
| [*SwitchList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchList.html) |  |  
| [*SwitchParser*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/SwitchParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*SwitchParser.Builder*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/SwitchParser.Builder.html) |  |  
| [*SwitchParserList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchParserList.html) |  |  
| [*SwitchValidator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchValidator.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](https://www.kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.15. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

