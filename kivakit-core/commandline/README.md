# kivakit-core-commandline &nbsp;&nbsp;![](../../documentation/images/command-line-40.png)

This project facilitates object-oriented parsing of command line arguments and switches.

![](documentation/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Dependencies**](#dependencies)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

The kivakit-core-commandline project is used by the [kivakit-core-application](../application/README.md) project to provide easy  
command line parsing to KivaKit applications. A detailed example on how to parse command line  
arguments and switches by extending the [*
Application*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.application/com/telenav/kivakit/core/application/Application.html)
class is available there.

[//]: # (end-user-text)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp;  ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-commandline</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp;![](documentation/images/diagram-48.png)

[*Argument Parsing*](documentation/diagrams/diagram-argument.svg)  
[*Command Line*](documentation/diagrams/diagram-command-line.svg)  
[*Command Line Validation*](documentation/diagrams/diagram-validation.svg)  
[*Switch Parsing*](documentation/diagrams/diagram-switch.svg)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.kivakit.core.commandline*](documentation/diagrams/com.telenav.kivakit.core.commandline.svg)  
[*com.telenav.kivakit.core.commandline.parsing*](documentation/diagrams/com.telenav.kivakit.core.commandline.parsing.svg)  
[*com.telenav.kivakit.core.commandline.project*](documentation/diagrams/com.telenav.kivakit.core.commandline.project.svg)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*
ApplicationMetadata*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/ApplicationMetadata.html) |  |  
| [*
Argument*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/Argument.html) |  |  
| [*
ArgumentList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentList.html) |  |  
| [*
ArgumentParser*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*
ArgumentParser.Builder*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/ArgumentParser.Builder.html) |  |  
| [*
ArgumentParserList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/ArgumentParserList.html) |  |  
| [*
ArgumentValidator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/ArgumentValidator.html) |  |  
| [*
CommandLine*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/CommandLine.html) | Retrieving Switches and Arguments |  
| | Error Handling |  
| [*
CommandLineParser*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/CommandLineParser.html) | Example |  
| | Parsing a Command Line |  
| | Switch Conventions |  
| [*
CoreCommandLineProject*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/project/CoreCommandLineProject.html) |  |  
| [*
Quantifier*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/Quantifier.html) |  |  
| [*
Switch*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/Switch.html) |  |  
| [*
SwitchList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchList.html) |  |  
| [*
SwitchParser*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/SwitchParser.html) | Parser Builders |  
| | Built-In Parsers |  
| [*
SwitchParser.Builder*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/SwitchParser.Builder.html) |  |  
| [*
SwitchParserList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchParserList.html) |  |  
| [*
SwitchValidator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.commandline/com/telenav/kivakit/core/commandline/parsing/SwitchValidator.html) |  |  

[//]: # (start-user-text)


[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>Copyright 2011-2021 [Telenav](http://telenav.com), Inc. Licensed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by Lexakai on 2021.04.01</sub>    
<sub>UML diagrams courtesy of PlantUML (http://plantuml.com)</sub>

