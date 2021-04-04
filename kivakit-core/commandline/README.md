# kivakit-core commandline &nbsp;&nbsp;![](../../documentation/images/command-line-40.png)

This project facilitates object-oriented parsing of command line arguments and switches.

![](documentation/images/horizontal-line.png)

### Index

[**Dependencies**](#dependencies)  
[**Summary**](#summary)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

### Dependencies &nbsp;&nbsp; ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-commandline</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

The kivakit-core-commandline project is used by the [kivakit-core-application](../application/README.md) project to provide easy  
command line parsing to KivaKit applications. A detailed example on how to parse command line  
arguments and switches by extending the [*
Application*](https://telenav.github.io/kivakit/javadoc/kivakit.core.application/com/telenav/kivakit/core/application/Application.html)
class is available there.

[//]: # (end-user-text)

### Class Diagrams &nbsp; &nbsp;![](documentation/images/diagram-48.png)

[*Argument Parsing*](documentation/diagrams/diagram-argument.svg)  
[*Command Line*](documentation/diagrams/diagram-command-line.svg)  
[*Command Line Validation*](documentation/diagrams/diagram-validation.svg)  
[*Switch Parsing*](documentation/diagrams/diagram-switch.svg)  

### Package Diagrams &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.kivakit.core.commandline*](documentation/diagrams/com.telenav.kivakit.core.commandline.svg)  
[*com.telenav.kivakit.core.commandline.parsing*](documentation/diagrams/com.telenav.kivakit.core.commandline.parsing.svg)  
[*com.telenav.kivakit.core.commandline.project*](documentation/diagrams/com.telenav.kivakit.core.commandline.project.svg)  

### Javadoc &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*ApplicationMetadata*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ApplicationMetadata.html) |  |  
| [*Argument*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Argument.html) |  |  
| [*ArgumentList*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentList.html) |  |  
| [*ArgumentParser*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*ArgumentParser.Builder*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentParser.Builder.html) |  |  
| [*ArgumentParserList*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/ArgumentParserList.html) |  |  
| [*ArgumentValidator*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/ArgumentValidator.html) |  |  
| [*CommandLine*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/CommandLine.html) | Retrieving Switches and Arguments |  
| | Error Handling |  
| [*CommandLineParser*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/CommandLineParser.html) | Example |  
| | Parsing a Command Line |  
| | Switch Conventions |  
| [*CoreCommandLineProject*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/project/CoreCommandLineProject.html) |  |  
| [*Quantifier*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Quantifier.html) |  |  
| [*Switch*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/Switch.html) |  |  
| [*SwitchList*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchList.html) |  |  
| [*SwitchParser*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/SwitchParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*SwitchParser.Builder*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/SwitchParser.Builder.html) |  |  
| [*SwitchParserList*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchParserList.html) |  |  
| [*SwitchValidator*](https://telenav.github.io/kivakit/javadoc/kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchValidator.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>Copyright 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.04. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

