# kivakit-core application &nbsp;&nbsp; <img src="https://www.kivakit.org/images/window-40.png" srcset="https://www.kivakit.org/images/window-40-2x.png 2x"/>

This module contains base classes for applications and servers. It provides command line parsing and enables  
configuration management and object registration and lookup.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**How to Create an Application**](#how-to-create-an-application)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/application/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-application</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

KivaKit applications (desktop and server) are created by extending the Application or Server base classes.
These classes provide access to:

>- Project initialization
>- Messaging and logging
>- Application configuration and settings
>- Usage information and help
>- Command line parsing
>- Maven project properties
>- Build information
>- Object registration and lookup

For an example application, see [**How to Create an Application**](#how-to-create-an-application) below, or 
browse the [**Javadoc**](#javadoc).

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Applications*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/application/documentation/diagrams/diagram-application.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.core.application*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/application/documentation/diagrams/com.telenav.kivakit.core.application.svg)  
[*com.telenav.kivakit.core.application.project*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/application/documentation/diagrams/com.telenav.kivakit.core.application.project.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 100.0%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-100-12.png)



| Class | Documentation Sections |
|---|---|
| [*Application*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.application/com/telenav/kivakit/core/application/Application.html) | Configuration |  
| | Command Line Parsing |  
| | Running |  
| | Messaging |  
| | Important Note: Project Initialization |  
| | Lookup |  
| | Startup |  
| | Settings |  
| [*ApplicationIdentifier*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.application/com/telenav/kivakit/core/application/ApplicationIdentifier.html) |  |  
| [*CoreApplicationProject*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.application/com/telenav/kivakit/core/application/project/CoreApplicationProject.html) |  |  
| [*Server*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.application/com/telenav/kivakit/core/application/Server.html) |  |  

[//]: # (start-user-text)

---

### How to Create an Application <a name="how-to-create-an-application"></a>

This section shows how to build a basic KivaKit application that counts lines in a text file in three simple steps.

#### 1. Create the Application Class &nbsp; &nbsp; <img src="https://www.kivakit.org/images/wand-32.png" srcset="https://www.kivakit.org/images/wand-32-2x.png 2x"/>

KivaKit applications extend _Application_, which provides base services to application subclasses.
The application must pass the name of a _Project_ to the _Application_ constructor. This ensures
that the services that the application needs to run get properly initialized for use. In our case,
we will be reading a file, so we need to initialize the _KivaCoreResources_ project. This will also
initialize anything that this project depends on (like _KivaCoreKernel_). Once the application is
constructed, the _run(String\[\])_ method is called, passing in the command line arguments that
were received from the JVM in _main()._

    public class LineCountApplication extends Application
    {
        public static void main(final String[] arguments)
        {
            new LineCountApplication().run(arguments);
        }

        private LineCountApplication()
        {
            super(KivaCoreResources.get());
        }

        [...]
    }

#### 2. Add Command Line Parsers to the Application Class &nbsp; &nbsp; <img src="https://www.kivakit.org/images/command-line-32.png" srcset="https://www.kivakit.org/images/command-line-32-2x.png 2x"/>

Here we add two fields, one is an argument parser for the input file, and the other
is a switch parser for the *-show-file-size* switch (if this switch is true, we will show
the file size).

Argument and switch parsers should never be declared as static members. This is
important because some argument parsers may depend on initialization that occurs
in the Application base class, which can cause those parsers to fail.

The methods _argumentParsers()_ and _switchParsers()_ return these parsers to the
_Application_ class, so it can parse the command line. Note that _ArgumentParser_
and _SwitchParser_ both parse objects from the command line, not strings.

    public class LineCountApplication extends Application
    {
        [...]

        private final ArgumentParser<File> INPUT =
                File.argumentParser("Input text file")
                        .required()
                        .build();

        private final SwitchParser<Boolean> SHOW_FILE_SIZE =
                SwitchParser.booleanSwitch("show-file-size", "Show the file size in bytes")
                        .optional()
                        .defaultValue(false)
                        .build();

        @Override
        protected List<ArgumentParser<?>> argumentParsers()
        {
            return List.of(INPUT);
        }

        @Override
        protected Set<SwitchParser<?>> switchParsers()
        {
            return Set.of(SHOW_FILE_SIZE);
        }

        [...]

#### 2. Create the Application Entrypoint &nbsp; &nbsp; <img src="https://www.kivakit.org/images/rocket-32.png" srcset="https://www.kivakit.org/images/rocket-32-2x.png 2x"/>

The method onRun() is called once command line switches are parsed. At this point
many convenient methods in the _Application_ base class will work. See _Application.java_
for detailed javadoc on these methods.

In our example, we retrieve the input file with the _argument(ArgumentParser)_
method, and we determine if we should show the file size with _get(SwitchParser)_.
Notice that the method _information(String message, Object... arguments)_ is being
used. This is because _Application_ is a _BaseRepeater_ and so it inherits all the
useful methods for broadcasting and listening to messages.

    public class LineCountApplication extends Application
    {
        @Override
        protected void onRun()
        {
            final var input = argument(INPUT);

            if (get(SHOW_FILE_SIZE))
            {
                information("File size = $", input.size());
            }

            information("Lines = $", Count.of(input.lines()));
        }

        [...]

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

