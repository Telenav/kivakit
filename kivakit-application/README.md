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

# kivakit-application &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/window-48.png" srcset="https://telenav.github.io/telenav-assets/images/icons/window-48-2x.png 2x"/>

This module contains base classes for applications and servers. It provides command line parsing and enables  
configuration management and object registration and lookup.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**How to Create an Application**](#how-to-create-an-application)  

[**Dependencies**](#dependencies) | [**Code Quality**](#code-quality) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.8.7/lexakai/kivakit/kivakit-application/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-application</artifactId>
        <version>1.8.7</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

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

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Code Quality <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/ruler-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/ruler-32-2x.png 2x"/>

Code quality for this project is 61.1%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-60-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-60-96-2x.png 2x"/>

| Measurement   | Value                    |
|---------------|--------------------------|
| Stability     | 83.3%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-80-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-80-96-2x.png 2x"/>     |
| Testing       | 16.7%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-20-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-20-96-2x.png 2x"/>       |
| Documentation | 83.3%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-80-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-80-96-2x.png 2x"/> |

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*Applications*](https://www.kivakit.org/1.8.7/lexakai/kivakit/kivakit-application/documentation/diagrams/diagram-application.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.application*](https://www.kivakit.org/1.8.7/lexakai/kivakit/kivakit-application/documentation/diagrams/com.telenav.kivakit.application.svg)  
[*com.telenav.kivakit.application.internal.lexakai*](https://www.kivakit.org/1.8.7/lexakai/kivakit/kivakit-application/documentation/diagrams/com.telenav.kivakit.application.internal.lexakai.svg)

### Javadoc <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

| Class | Documentation Sections  |
|-------|-------------------------|
| [*Application*](https://www.kivakit.org/1.8.7/javadoc/kivakit/kivakit-application/com/telenav/kivakit/application/Application.html) | Application Metadata |  
| | Application Environment |  
| | Project Initialization |  
| | Execution |  
| | Messaging and Logging |  
| | Creating an Application |  
| | Command Line Parsing |  
| | Abnormal Termination |  
| [*Application.Identifier*](https://www.kivakit.org/1.8.7/javadoc/kivakit/kivakit-application/com/telenav/kivakit/application/Application.Identifier.html) |  |  
| [*Application.LifecyclePhase*](https://www.kivakit.org/1.8.7/javadoc/kivakit/kivakit-application/com/telenav/kivakit/application/Application.LifecyclePhase.html) |  |  
| [*ApplicationExit*](https://www.kivakit.org/1.8.7/javadoc/kivakit/kivakit-application/com/telenav/kivakit/application/ApplicationExit.html) |  |  
| [*DiagramApplication*](https://www.kivakit.org/1.8.7/javadoc/kivakit/kivakit-application/com/telenav/kivakit/application/internal/lexakai/DiagramApplication.html) |  |  
| [*ExitCode*](https://www.kivakit.org/1.8.7/javadoc/kivakit/kivakit-application/com/telenav/kivakit/application/ExitCode.html) |  |  
| [*Server*](https://www.kivakit.org/1.8.7/javadoc/kivakit/kivakit-application/com/telenav/kivakit/application/Server.html) |  |  

[//]: # (start-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

<a name="how-to-create-an-application"></a>
### How to Create an Application <a name = "how-to-create-an-application"></a>

This section shows how to build a basic KivaKit application that counts lines in a text file in three simple steps.

#### 1. Create the Application Class &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/wand-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/wand-32-2x.png 2x"/>

KivaKit applications extend `Application`, which provides base services to application subclasses.
The application must pass the name of a `Project` to the `Application` constructor. This ensures
that the services that the application needs to run get properly initialized for use. In our case,
we will be reading a file, so we need to initialize the `KivaCoreResources` project. This will also
initialize anything that this project depends on (like `KivaCoreKernel`). Once the application is
constructed, the `run(String\[\])` method is called, passing in the command line arguments that were received from the JVM in `main().`

    public class LineCountApplication extends Application
    {
        public static void main( String[] arguments)
        {
            new LineCountApplication().run(arguments);
        }

        private LineCountApplication()
        {
            super(KivaCoreResources.get());
        }

        [...]
    }

#### 2. Add Command Line Parsers to the Application Class &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/command-line-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/command-line-32-2x.png 2x"/>

Here we add two fields, one is an argument parser for the input file, and the other is a switch parser for the *-show-file-size* switch (if this switch is true, we will show the file size).

Argument and switch parsers should never be declared as static members. This is important because some argument parsers may depend on initialization that occurs in the Application base class, which can cause those parsers to fail.

The methods `argumentParsers()` and `switchParsers()` return these parsers to the `Application` class, so it can parse the command line. Note that `ArgumentParser` and `SwitchParser` both parse objects from the command line, not strings.

    public class LineCountApplication extends Application
    {
        [...]

        private final ArgumentParser<File> INPUT =
                File.argumentParser("Input text file")
                        .required()
                        .build();

        private final SwitchParser<Boolean> SHOW`FILE_SIZE =
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

#### 2. Create the Application Entrypoint &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/rocket-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/rocket-32-2x.png 2x"/>

The method onRun() is called once command line switches are parsed. At this point
many convenient methods in the `Application` base class will work. See `Application.java` for detailed Javadoc on these methods.

In our example, we retrieve the input file with the `argument(ArgumentParser)` method, and we determine if we should show the file size with `get(SwitchParser)`. Notice that the method `information(String message, Object... arguments)` is being
used. This is because `Application` is a `BaseRepeater` and so it inherits all the useful methods for broadcasting and listening to messages.

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

<a name="application-lifecycle"></a>
<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

A application lifecycle follows this sequence.

<img src="https://telenav.github.io/telenav-assets/images/diagrams/application-lifecycle@2x.png"/>

#### CONSTRUCTING

1. The method `main(String[] arguments)` is called
2. The application enters the `CONSTRUCTING` phase
3. The application is constructed
4. The `run(String[])` method is called to execute the application

#### INITIALIZING

1. The application enters the `INITIALIZING` phase
2. `onInitializing()` is called 
3. `startupOptions()` is called and the set of `StartupOptions` returned is enabled
4. `onSerializationInitialize()` is called to allow registration of serializers
5. Any `Deployment` configurations are loaded from the `deployments` package
6. The command line arguments passed to `main()` are parsed into a `CommandLine` using
   switch and argument parsers returned by `switchParsers()` and `argumentParsers()`
7. If a `-deployment` was specified, it is loaded and registered with the global settings registry
8. `onInitialize()` is called to initialize the application
9. `onProjectsInitializing()` is called to notify that projects will be initializing
10. `onProjectsInitialize` is called
11. The default implementation of `onProjectsInitialize()` initializes projects
12. `onProjectsInitialized()` is called
13. `onInitialized()` is called to notify that initialization is complete

#### RUNNING

1. The application enters the `RUNNING` phase
2. `onRunning()` is called to notify that the application is about to run

#### READY

1. The application enters the `READY` phase
2. `onRun()` is called to run the application

#### STOPPING

1. The application enters the `STOPPING` phase
2. All logs are flushed
3. Message statistics are displayed (how many glitches, warnings, problems, etc.)
4. `onRan()` is called

#### STOPPED

1. The application transitions to the `STOPPED` state
2. The application exits. If no exception was thrown and exit wasn't called, it exits with status code 0 (no error), otherwise it exits with a non-zero status code.

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
