# kivakit-core-kernel logging &nbsp; ![](../../../documentation/images/log-40.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.logging*

### Index

[**Summary**](#summary)  
[**Creating a Logger**](#creating-a-logger)  
[**Logging Messages**](#logging-messages)  
[**Logging Broadcast Messages**](#logging-broadcast-messages)  
[**Message Formatting**](#message-formatting)  
[**Simplified Stack Traces**](#simplified-stack-traces)  
[**Log Levels**](#log-levels)  
[**Different Kinds of Logs**](#different-kinds-of-logs)  
[**Log Level Overrides**](#different-kinds-of-logs)  
[**Creating New Logs**](#creating-new-logs)  
[**Log Viewer**](#log-viewer)

![](../documentation/images/horizontal-line.png)

### Summary <a name="summary"></a>

KivaKit logging API is a bit different from your typical logging framework. It is powerful and  
object-oriented and has been designed to integrate with the rest of KivaKit. It is suggested   
that KivaKit logging be used instead of Log4j, Commons Logging, Logback, etc. The reason for this  
hopefully will become apparent below.

### Creating a Logger <a name="creating-a-logger"></a>

You can create a logger in a given class with this boilerplate definition:

    private static final Logger LOGGER = LoggerFactory.newLogger();

To reduce typing, an IntelliJ live template such as the "logger" template in:

    $KIVAKIT_HOME/setup/intellij/live-templates.zip

can quickly declare a logger. The logger will inspect the call stack when it is constructed and   
use this context information when creating log entries.

Most loggers in KivaKit are _asynchronous_. This is _very important_ to performance because the  
thread logging the message does not block. Since the log entry is logged in the background, things  
can occasionally become a little confusing when debugging a multi-threaded application. This  
system property will cause logging to happen synchronously, blocking the logging thread until  
the log has been written to:

    -DKIVAKIT_LOG_SYNCHRONOUS=true

Don't forget if you set this flag, because it can hurt performance significantly.

### Logging Messages <a name="logging-messages"></a>

To log a message, there is similar boilerplate for each message type (see [Messaging](messaging.md)) which you  
can insert quickly using IntelliJ live templates:

    LOGGER.problem("Database down at $", Time.now())); 
    LOGGER.problem(exception, "Socket $ unexpectedly closed", socket);
    LOGGER.warning("Unable to manufacture the widget $ for less than $", widget, cost);

This design has some advantages over text loggers. One is _efficiency_. Construction of this object  
doesn't require that the message be formatted (where the time is converted to a string and substituted  
for $) before it's passed to the logging system. Construction of a _Problem_ object is fast (object  
allocation and 8 field assignments) and doesn't involve any parsing or string concatenation. The  
message is only formatted once it's been determined that the message is not filtered out (for example,  
by a **KIVAKIT_LOG_LEVEL** property), and it has reached a log. This efficiency means that it's usually   
okay to put a lot of informative log statements in your code! You won't pay much of a penalty for  
doing this. When you filter out unwanted messages with **KIVAKIT_LOG_LEVEL**, only the minimal object  
construction overhead will remain. For example:

    LOGGER.information("Loaded $ in $: $", file, start.elapsedSince(), willTakeForeverToConvertToAString);

will take a negligible amount of time to construct. If you have _Information_ messages filtered out,  
the message will never be logged, and the object _willTakeForeverToConvertToAString_ will never  
actually be converted to a string because the message will never be formatted since it will never  
reach a log.

You can also use lambda functions to delay the execution of a method until the message is written  
to a log:

    LOGGER.trace("PI = $", Source.of(() -> computeDigitsOfPi(1_000_000)));

This will have virtually no impact until tracing is turned on. There are further conveniences and  
efficiencies regarding debug tracing to be found in the next section [Debugging](debugging.md).

### Logging Broadcast Messages <a name="logging-broadcast-messages"></a>

Note that a _Logger_ is a _Listener_ (see [Messaging](messaging.md) and can will log any message it hears (if it  
isn't filtered out). So you can log all the messages broadcast by an object with code like:

    LOGGER.listenTo(computeServer);

### Message Formatting  <a name="message-formatting"></a>

When messages are formatted at log time, object parameters at the end of the message are interpolated  
(substituted) into the message. In addition to the simple substitution $, there are a number of other  
helpful commands you can use:

* ${class} - the simple class name of the argument (not the fully qualified one)
* ${object} - the object as formatted by ObjectFormatter into a standard property list format
* ${debug} - the result of calling toDebugString on the object through the DebugString interface
* ${flag} - "enabled" or "disabled" for a boolean argument
* ${name} - the object name as retrieved by a name() method through the Named interface
* ${hex} - the integral object in hexadecimal
* ${binary} - the integral object in binary
* ${long} - the long value without comma-separation

### Simplified Stack Traces <a name="simplified-stack-traces"></a>

Stack traces can be simplified to the minimum with *-DKIVAKIT_SIMPLIFIED_STACK_TRACES=true*.

    BigBitArray allocated ObjectArray(65,536) ≡24
    (PrimitiveCollection.java:621) allocated
    (PrimitiveCollection.java:604) allocated
    (PrimitiveCollection.java:142) <init>
    (ObjectArray.java:54) <init>
    (BigBitArray.java:17) <init>
    (PbfTagStore.java:21) <init>
    (GraphElementStore.java:236) pbfTagCodec
    (Graph.java:1356) tagCodecs
    (RawPbfGraphLoader.java:377) onLoad
    (GraphLoader.java:44) load
    (Graph.java:891) load
    (OsmPbfGraphLoader.java:50) onLoad
    (GraphLoader.java:44) load
    (Graph.java:891) load
    (PbfToGraphConverter.java:277) load
    (PbfToGraphConverter.java:102) convert
    (PbfToGraphConverter.java:133) convert
    (PbfToGraphConverterApplication.java:217) convertOne
    (PbfToGraphConverterApplication.java:151) run
    (PbfToGraphConverterApplication.java:76) main

### Log Levels <a name="log-levels"></a>

In addition, the _LoggerFactory_ looks for the switch **KIVAKIT_LOG_LEVEL** to set default filtering of log  
messages. This can be overridden from the command line for individual logs.

Anything at or above the severity of the given message will be logged, anything below that severity  
will be discarded.

Log levels are specified by the message name, including:

* **Problem**
* **Warning**
* **Quibble**
* **Announcement**
* **Narration**
* **Information**
* **Trace**
* **Alert**
* **CriticalAlert**
* **OperationsAlert**

So if you wanted to turn tracing on while debugging something, you could say:

    -DKIVAKIT_LOG_LEVEL=Trace

In a production environment, you might want:

    -DKIVAKIT_LOG_LEVEL=Warning

A production machine will experience only very minimal impact from trace messages, so there's usually  
no need for conditionals like *if (DEBUG) { trace... }*. However, when it's necessary, this construct  
can be used:

    if (DEBUG.isEnabled())
    { 
        trace("checking indexes"); 
        reallyExpensiveIndexCheckingOperation(); 
        trace("done checking indexes"); 
    }

### Different Kinds of Logs <a name="different-kinds-of-logs"></a>

The _LoggerFactory_ uses Java Services to locate and load _Log_ service providers.

To use KivaKit log service providers, you must reference them in your pom.xml.

If your application is modular, you must also _require_ each service provider you want to use  
in your module-info.java class.

_LoggerFactory_ looks at the system property **KIVAKIT_LOG** and adds one or more (comma separated) logs.  
It also explains what logs are available.

* **Console** - Formatted logging to the console
    * formatter=columnar|unformatted (default is columnar)
* **File** - File log. Parameters:
    * file=/var/logs/myapp.txt - path to file
    * rollover=none|hourly|daily - when to rollover to a new file (none is default)
    * maximum-size=<size> - maximum size of log before it rolls over (default is "50M")
* **Viewer** - Shows the log viewer, as in the screenshot below. Since your application can cause  
  the viewer to fail, it may be better to run it in another process. See the _Log Viewer_ section below.
* **Server** - Makes log entries available on a server (with no UI) for clients. Clients can connect  
  to the server and view the log with the _Log Viewer_ (see below). *maximum-entries=[count]* -  
  the maximum number of entries to hold in memory for clients to access (default is 20,000)
* **Email** - Sends emails for each log message
    * subject - subject of the email
    * from - email address
    * to - comma separated list of email addresses
    * host - SMTP host
    * username - username on the host
    * password - password to send email

### Log Level Overrides <a name="log-level-overrides"></a>

All logs take a parameter _level=<message-type>_ where <message-type> is the minimum level of messages  
to be logged to the given log. It can be any kind of broadcastable object extending Message. For example,  
Problem, Warning, Quibble, Trace, etc...

### Examples

To log all entries to the console, Warnings and higher to a rollover file log and Quibbles and higher  
to a server log that can be viewed with the log viewer, you would use the first system property here.  
The other properties give additional examples.

    -DKIVAKIT_LOG="Console,File level=Warning file=~/log.txt rollover=daily maximum-size=100M,Server level=Quibble"

    -DKIVAKIT_LOG="Server level=Warning maximum-entries=1.1.2-SNAPSHOT,Email level=CriticalAlert subject=Alert from=jonathanl@telenav.com to=jonathanl@telenav.com host=smtp.telenav.com username=jonathanl@telenav.com password=shibo"

### Creating New Logs <a name="creating-new-logs"></a>

It is not hard to implement a new kind of log if you need one. If you derive your _Log_ from  
_AbstractLog_ or _AbstractTextLog_, the only methods you need to implement are:

    public interface Log 
    { 
        void log(LogEntry entry);    
        void configure(Map<String, String> properties); 
    }

Your module will also need to provide the _Log_ implementation with a _provides_ statement.  
See any of the logs in KivaKit for an example.

### Log Viewer <a name="log-viewer"></a>

If you want to see your logging output in a Swing searchable popup window, start your server with:

    -DKIVAKIT_LOG=Server

The server will queue up log entries, which you can view with KivaKit log viewer:

### Download Log Viewer &nbsp; ![](../../../documentation/images/down-arrow-32.png)

[**KivaKit Log Viewer**](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/bin/kivakit-log-viewer-0.9.0-SNAPSHOT.jar)

<br/>

![](../documentation/images/horizontal-line.png)
