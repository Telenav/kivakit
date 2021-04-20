# kivakit-core commandline &nbsp;&nbsp; <img src="https://www.kivakit.org/images/command-line-32.png" srcset="https://www.kivakit.org/images/command-line-32-2x.png"/>

This project facilitates object-oriented parsing of command line arguments and switches.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/commandline/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-commandline</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

The *kivakit-core commandline* project is used by the [kivakit-core-application](../application/README.md) project to provide easy
command line parsing to KivaKit applications. A detailed example on how to parse command line
arguments and switches is available in the Javadoc for the [*Application*](https://telenav.github.io/kivakit/javadoc/kivakit.core.application/com/telenav/kivakit/core/application/Application.html) class.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Argument Parsing*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/commandline/documentation/diagrams/diagram-argument.svg)  
[*Command Line*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/commandline/documentation/diagrams/diagram-command-line.svg)  
[*Command Line Validation*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/commandline/documentation/diagrams/diagram-validation.svg)  
[*Switch Parsing*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/commandline/documentation/diagrams/diagram-switch.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.core.commandline*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/commandline/documentation/diagrams/com.telenav.kivakit.core.commandline.svg)  
[*com.telenav.kivakit.core.commandline.parsing*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/commandline/documentation/diagrams/com.telenav.kivakit.core.commandline.parsing.svg)  
[*com.telenav.kivakit.core.commandline.project*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/commandline/documentation/diagrams/com.telenav.kivakit.core.commandline.project.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 91.4%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-90-96.png" srcset="https://www.kivakit.org/images/meter-90-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*ApplicationMetadata*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ApplicationMetadata.html) |  |  
| [*Argument*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Argument.html) |  |  
| [*ArgumentList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentList.html) |  |  
| [*ArgumentParser*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*ArgumentParser.Builder*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentParser.Builder.html) |  |  
| [*ArgumentParserList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/ArgumentParserList.html) |  |  
| [*ArgumentValidator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/ArgumentValidator.html) |  |  
| [*CommandLine*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/CommandLine.html) | Retrieving Switches and Arguments |  
| | Error Handling |  
| [*CommandLineParser*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/CommandLineParser.html) | Example |  
| | Parsing a Command Line |  
| | Switch Conventions |  
| [*CoreCommandLineProject*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/project/CoreCommandLineProject.html) |  |  
| [*Quantifier*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Quantifier.html) |  |  
| [*Switch*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Switch.html) |  |  
| [*SwitchList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchList.html) |  |  
| [*SwitchParser*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/SwitchParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*SwitchParser.Builder*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/SwitchParser.Builder.html) |  |  
| [*SwitchParserList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchParserList.html) |  |  
| [*SwitchValidator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchValidator.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

