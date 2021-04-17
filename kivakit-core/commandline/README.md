# KivaKit Core - Command Line &nbsp;&nbsp;![](https://www.kivakit.org/images/command-line-40.png)

This project facilitates object-oriented parsing of command line arguments and switches.

![](https://www.kivakit.org/images/horizontal-line.png)

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://www.kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit-core/commandline/diagrams/dependencies.svg)

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

[*Argument Parsing*](https://www.kivakit.org/lexakai/diagrams/diagram-argument.svg)
  [*Command Line*](https://www.kivakit.org/lexakai/diagrams/diagram-command-line.svg)
  [*Command Line Validation*](https://www.kivakit.org/lexakai/diagrams/diagram-validation.svg)
  [*Switch Parsing*](https://www.kivakit.org/lexakai/diagrams/diagram-switch.svg)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/box-40.png)

[*com.telenav.kivakit.core.commandline*](https://www.kivakit.org/lexakai/diagrams/com.telenav.kivakit.core.commandline.svg)
  [*com.telenav.kivakit.core.commandline.parsing*](https://www.kivakit.org/lexakai/diagrams/com.telenav.kivakit.core.commandline.parsing.svg)
  [*com.telenav.kivakit.core.commandline.project*](https://www.kivakit.org/lexakai/diagrams/com.telenav.kivakit.core.commandline.project.svg)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/books-40.png)

Javadoc coverage for this project is 91.4%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-90-12.png)



| Class | Documentation Sections |
|---|---|
| [*ApplicationMetadata*](null/com/telenav/kivakit/core/commandline/ApplicationMetadata.html) |  |  
| [*Argument*](null/com/telenav/kivakit/core/commandline/Argument.html) |  |  
| [*ArgumentList*](null/com/telenav/kivakit/core/commandline/ArgumentList.html) |  |  
| [*ArgumentParser*](null/com/telenav/kivakit/core/commandline/ArgumentParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*ArgumentParser.Builder*](null/com/telenav/kivakit/core/commandline/ArgumentParser.Builder.html) |  |  
| [*ArgumentParserList*](null/com/telenav/kivakit/core/commandline/parsing/ArgumentParserList.html) |  |  
| [*ArgumentValidator*](null/com/telenav/kivakit/core/commandline/parsing/ArgumentValidator.html) |  |  
| [*CommandLine*](null/com/telenav/kivakit/core/commandline/CommandLine.html) | Retrieving Switches and Arguments |  
| | Error Handling |  
| [*CommandLineParser*](null/com/telenav/kivakit/core/commandline/CommandLineParser.html) | Example |  
| | Parsing a Command Line |  
| | Switch Conventions |  
| [*CoreCommandLineProject*](null/com/telenav/kivakit/core/commandline/project/CoreCommandLineProject.html) |  |  
| [*Quantifier*](null/com/telenav/kivakit/core/commandline/Quantifier.html) |  |  
| [*Switch*](null/com/telenav/kivakit/core/commandline/Switch.html) |  |  
| [*SwitchList*](null/com/telenav/kivakit/core/commandline/parsing/SwitchList.html) |  |  
| [*SwitchParser*](null/com/telenav/kivakit/core/commandline/SwitchParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*SwitchParser.Builder*](null/com/telenav/kivakit/core/commandline/SwitchParser.Builder.html) |  |  
| [*SwitchParserList*](null/com/telenav/kivakit/core/commandline/parsing/SwitchParserList.html) |  |  
| [*SwitchValidator*](null/com/telenav/kivakit/core/commandline/parsing/SwitchValidator.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](https://www.kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.16. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

