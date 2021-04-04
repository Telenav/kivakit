# kivakit-core-kernel language.vm &nbsp; ![](../../../documentation/images/jvm-56.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.language.vm*

### Index

[**Summary**](#summary)  
[**The Java Virtual Machine**](#the-java-virtual-machine)  
[**The Operating System**](#the-operating-system)  
[**Processes**](#processes)

![](../documentation/images/horizontal-line.png)

### Summary <a name="summary"></a>

This package contains classes relevant to the Java virtual machine (VM), the operating system  
it runs on, and the processes that run on the OS.

### The Java Virtual Machine

The class *JavaVirtualMachine* provides access to:

* System properties
* Java instrumentation and approximate sizeof functionality
* Thread status
* VM health in the form of *JavaVirtualMachineHealth*
* Information about resources such as free memory and processors

In addition, the *KivaShutdownHook* provides basic ordered execution of Java shutdown hooks  
as one of the set of 'first' executed hooks or one of the set of 'last' shutdown hooks.  
The idea is to provide the ability to ensure that one of two (or more) hooks runs before the  
other(s).

### The Operating System

The *OperatingSystem* class provides:

* OS name and type
* 'Exec' functionality that captures output as a string
* Location of Java executable and JAVA_HOME

### Processes

The *Processes* class provides methods that redirect and capture process output.

<br/>

![](../documentation/images/horizontal-line.png)
