# kivakit-core-kernel messaging &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/envelope-40.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.messaging*

### Index

[**Summary**](#summary)  
[**Broadcasters**](#broadcasters)  
[**Listeners**](#listeners)  
[**Repeaters**](#repeaters)  
[**Messages**](#messages)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

KivaKit has an implementation of the *broadcaster / listener* design pattern which is used  
extensively throughout KivaKit to provide loose-coupled integration between components.

A few examples of objects in KivaKit that transmit and receive messages:

* Loggers
* Extractors
* Converters
* Validators
* Resources
* Progress reporters
* Graphs
* Graph loaders
* Data readers

### Broadcasters <a name="broadcasters"></a> &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/sonar-32.png)

A _Broadcaster_ transmits messages that implement the _Broadcastable_ tag interface to one or  
more _Listeners_. This is the key method in the _Broadcaster_ interface:

    public interface Broadcaster
    { 
        void transmit(Transmittable message); 
    }

The method _transmit()_ sends the given message to all objects that are listening in.

### Listeners <a name="listeners"></a> &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/ear-32.png)

A _Listener_ registers interest in a _Broadcaster_ with the _listenTo()_ method (or if it's more  
convenient there is a method in _Broadcaster_, _broadcastTo()_ that accomplishes the same thing):

    public interface Listener
    {     
        void listenTo( Broadcaster broadcaster);
        void receive(Transmittable message); 
    }

After registering as a listener to a broadcaster, the _Broadcaster_ will call the _onReceive()_  
method of the listener when it has a message to deliver.

### Repeaters <a name="repeaters"></a>

A _Repeater_ is both a _Broadcaster_ and a _Listener_:

    public interface Repeater extends Listener, Broadcaster
    { 
        [...]
    }

It does just what you'd think. It broadcasts all the message that it hears.

A base class, _BaseRepeater_, is commonly used to implement messaging functionality in classes.  
In cases where an object is already extending some other object, a _BaseRepeater_ may be aggregated  
in the object to implement the _Repeater_ interface.

### Messages <a name="messages"></a> &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/envelope-32.png)

A _Message_ is the most important kind of _Broadcastable_, and it is used in logging and for other purposes:

    public interface Message implements Broadcastable 
    { 
        public Object[] arguments(); 
        Throwable cause(); 
        Time created();  
        String formattedMessage(); 
        String message(); 
        Severity severity(); 
    }

A few messages commonly used in logging include:

    public class Problem implements Message { ... }
    public class Warning implements Message { ... } 
    public class Quibble implements Message { ... } 
    public class Information implements Message { ... } 
    public class Trace implements Message { ... }

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
