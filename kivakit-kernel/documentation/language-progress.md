# kivakit-core-kernel language.progress &nbsp; ![](../../../documentation/images/progress-48.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.language.progress*

### Index

[**Summary**](#summary)  
[**Reporting Progress**](#reporting-progress)  
[**Listening to Progress**](#listening-to-progress)

![](../documentation/images/horizontal-line.png)

### Summary <a name="summary"></a>

This package defines how the progress of long-running operations is reported and displayed.

### Reporting Progress <a name="reporting-progress"></a> &nbsp;  ![](../../../documentation/images/sonar-32.png)

A *ProgressReporter* is used by an operation to report its progress as the operation proceeds.  
Methods are provided to:

* Configure the reporter: the number of steps, and the *ProgressListener* to report to
* Report the start of the operation
* Report that a phase of the operation is underway
* Report progress in steps
* Report the end of the operation

The interface looks roughly like this:

    public interface ProgressReporter extends Resettable
    {
        // Configure
        default ProgressReporter listener( ProgressListener listener)
        default ProgressReporter steps( Count steps)
        default Count steps()
        default boolean isIndefinite()

        // Start operation
        default ProgressReporter start()
        default ProgressReporter start( String label)

        // Phase
        default ProgressReporter phase( String phase)

        // Report progress
        void next()
        default void next( Count steps)
        default void next( int steps)

        // End operation
        default void end( String label)
        default void end()
    }

The *Progress* object implements the *ProgressReporter* interface by implementing and overriding  
the interface methods and by broadcasting [*messages*](messaging.md) at regular intervals as the operation   
progresses:

    public class Progress extends Multicaster implements ProgressReporter
    {
        [...]
    }

### Listening To Progress <a name="listening-to-progress"></a>  &nbsp;  ![](../../../documentation/images/ear-32.png)

Any normal *Listener* can listen to and display progress from a *Progress* object like this:

    var reporter = Progress.create(listener);

Since *Logger* is a message listener, logging progress is simply this:

    var reporter = Progress.create(LOGGER); 

If reporting of a percentage-complete value is desired, the *ProgressReporter.listener(ProgressListener)*  
method can be used to register a *ProgressListener*:

    public interface ProgressListener
    {
        void at(Percent at)
    }

in addition to a message *Listener*, like this:

    reporter.listener(System.out::println);

<br/>

![](../documentation/images/horizontal-line.png)
