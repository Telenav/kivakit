# kivakit-core-kernel language.threading &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/chips-48.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.threading*

### Index

[**Summary**](#summary)  
[**Threads**](#threads)  
[**Thread Context**](#thread-context)  
[**Thread Status**](#thread-status)  
[**Locks**](#locks)  
[**Latches**](#latches)  
[**Conditions**](#conditions)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

This package provides classes for managing concurrency, creating and managing threads,  
and for determining their status.

### Threads <a name="threads"></a>

The *KivaThread* class integrates threads with the [*messaging*](messaging.md) mini-framework, allowing them  
to broadcast information about their status to interested listeners. It also provides useful methods to:

* Execute an operation repeatedly
* Start threads synchronously
* Query the time that the thread started, and get the elapsed time

In addition, all *KivaThread*s are *Named*, and the constructor prefixes the name with "KivaKit-", to ensure  
that its clear which threads were created by the framework.

The *RepeatingThread* class repeats an operation, but adds the ability to *pause()* and *resume()* it.

The *Threads* class contains generically useful thread operations such as:

* Enumerate all threads
* Create simple executor thread pools with KivaKit thread names
* Shutdown and await an executor

### Thread Context <a name="thread-context"></a>

The *context* sub-package contains code that simplifies access to stack traces as well as code  
which helps to examine the call stack.

### Thread Status <a name="thread-status"></a>

The *status* sub-package has classes to:

* Track thread reentrancy
* Retrieve information about thread status
* Represent reasons why a thread awakened

### Locks <a name="thread-locks"></a>

The *locks* subpackage contains classes that make it easier to use Java locks by adding lambdas   
to the mix. The *Lock* class extends *ReentrantLock* to provide a *whileLocked*(Runnable)* method,  
which can eliminate the need for direct calls to *lock()* and *unlock()*:

    var lock = new Lock()
    lock.whileLocked(this::accessData)

The *ReadWriteLock* subclasses *ReentrantReadWriteLock* and provides similar functionality:

    var lock = new ReadWriteLock();
    lock.read(this::readData);
    lock.write(this::writeData);

### Latches <a name="thread-latches"></a>

The *latched* sub-package provides simple wrappers to *CountDownLatch* that help to clarify code.

### Conditions <a name="conditions"></a>

The *conditions* sub-package provides classes that can be used to observe and wait for specific  
states under multithreading. The *ValueWatcher* class simplifies signaling that a value changed  
and waiting for the change.

The *StateMachine* class uses enum values to represent the states of some operation. The state  
machine starts in a given state when it is constructed. Then, as the operation proceeds, threads  
can transition to a new state, determine what the current state is, and they can wait for a particular  
state to be reached.

For example:

    enum State { UNBORN, LIVING, DEAD }
    
         [...]
    
     var person = new StateMachine(State.UNBORN);
    
         [...]
    
     KivaThread.run("Life", () ->
     {
         // Get born
         person.transitionTo(LIVING);
    
         // live life
         while (person.is(LIVING))
         {
             doSomething();
         }
    
         // then exit.
     });
    
     KivaThread.run("GrimReaper", () ->
     {
         // Wait a while
         Duration.years(random(101)).sleep();
    
         // then kill the person
         person.transitionTo(DEAD);
     });
    
     KivaThread.run("FuneralHome", () ->
     {
         // Wait for some business
         person.waitFor(DEAD);
    
         // then make some money.
         cashIn();
     });

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
