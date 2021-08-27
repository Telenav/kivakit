# kivakit-core-kernel debugging &nbsp; ![](../../../documentation/images/bug-40.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.messaging*

### Index

[**Summary**](#summary)  
[**Adding Debug Traces**](#adding-debug-traces)  
[**Conditional Code**](#conditional-code)  
[**Debugging Advertisements**](#debugging-advertisements)  
[**Enabling Debugging at Runtime**](#enabling-debugging-at-runtime)

![](../documentation/images/horizontal-line.png)

### Summary <a name="summary"></a>

KivaKit has a simple, but powerful class for controlling debug tracing in an application. The _Debug_ class works with the [broadcaster / listener mini-framework](messaging.md) and [kivakit-core-logging](logging.md). It can be used to turn debug tracing and other debug code on and off at runtime by way of the Java system property **KIVAKIT_DEBUG**.

### Adding Debug Traces <a name="adding-debug-traces"></a>

To add debug tracing to a class, one method is to make the following declarations of a _Logger_ and a _Debug_ object:

    private static final Logger LOGGER = LoggerFactory.newLogger(); 
    private static final Debug DEBUG = new Debug(LOGGER);

You can then add traces like this:

    DEBUG.trace("index = $", index);

using the same formatting style as in the logging system (there are numerous substitution expressions that can be used, for details, see [MessageFormatter](https://telenav.github.io/kivakit/javadoc/kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/MessageFormatter.html)). You can also add "*quibbles*" (a less important type of warning that is only enabled when debug tracing is turned on) and *warnings* in the same way.

### *Debug Tracing, Components and Messaging*

The method just described above is always usable to add debug tracing to a class, but it is not the preferred method in most cases because the *LOGGER* static field above doesn't participate in a listener chain (it is a terminal listener). The *BaseComponent* class (or *ComponentMixin* interface) and the *BaseRepeater* class (or *RepeaterMixin* interface) provide easy access to debug tracing through the inherited methods *trace()*, *isDebugOn()* and *ifDebug(Runnable)*:

    class MyComponent extends BaseComponent
    {
        public void myMethod()
        {
            trace("Executing my method");
        }
    }

Because all *Application*s are *BaseComponent*s, this functionality is automatically available to the methods of your application subclass.

### Conditional Code <a name="conditional-code"></a>

Occasionally it may be desirable to make whole blocks of code conditional as well. This can be done with the _isEnabled()_ method:

    if (DEBUG.isEnabled()) { ... }
    
or in a component with *isDebugOn()*:

    if (isDebugOn())
    {
        [...]
    }    

### Debugging Advertisements <a name="debugging-advertisements"></a>

When your application starts up, if debugging is enabled for the _Debug_ class, each class that has debugging output available will advertise its availability something like this:

    | ... | Debug output is available for AbstractResource (com.telenav.kivakit.kernel.framework.resource)
    | ... | Debug output is available for Folder (com.telenav.kivakit.kernel.framework.filesystem)
    | ... | Debug output is available for File (com.telenav.kivakit.kernel.framework.filesystem)

### Enabling Debugging at Runtime <a name="enabling-debugging-at-runtime"></a>

To enable debugging for one or more classes or packages in your application at runtime, simply add a system property like this:

    -DKIVAKIT_DEBUG=<pattern>

The *Debug* constructor inspects the call stack (only once per class) to see if the class that is constructing the *Debug* object matches a list of simplified regular expression patterns in the **KIVAKIT_DEBUG** system property. If there is a match, then debugging is enabled for that class.

In simplified regular expression patterns, dot (.) matches a literal dot instead of any character and star (\*) matches zero or more characters as if it was ".\*". Otherwise all regular expression features work as always.

Multiple patterns can be separated by commas, *not* negates the matching of a pattern, and *extends* can be used to match subclasses.

A few examples to demonstrate:

| Pattern | Effect |
|---|---|
| -DKIVAKIT_DEBUG=Debug | Turns on debug advertisements |
| -DKIVAKIT_DEBUG=Folder | Enables debugging in the class Folder |
| -DKIVAKIT_DEBUG=File,Folder | Enables debugging in both File and Folder classes |
| -DKIVAKIT_DEBUG="\*" | Enable debugging in all classes |
| -DKIVAKIT_DEBUG="\*,not Folder" | Enables debugging everywhere except the Folder class |
| -DKIVAKIT_DEBUG="extends Region" | Enable debugging in Region and all of its subclasses |
| -DKIVAKIT_DEBUG="Osm\*" | Enable debugging in all classes starting with "Osm" |
| -DKIVAKIT_DEBUG="\*Checker" | Enable debugging in all classes that end with "Checker" |
| -DKIVAKIT_DEBUG="com.myapp.\*" | Enable debugging in all classes in all packages below the given package |
| -DKIVAKIT_DEBUG="\*.filesystem.\*" | Enables debugging in all classes under all packages named "filesystem" |

<br/>

![](../documentation/images/horizontal-line.png)
