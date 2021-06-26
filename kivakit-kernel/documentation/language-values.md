# kivakit-core-kernel language.values &nbsp; ![](../../../documentation/images/value-48.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.language.values*

### Index

[**Summary**](#summary)  
[**Counts**](#counts)  
[**Identifiers**](#identifiers)  
[**Levels**](#levels)  
[**Mutable Values**](#mutable-values)  
[**Versions**](#versions)

![](../documentation/images/horizontal-line.png)

### Summary <a name="summary"></a>

This package provides a variety of value objects that are typesafe and make code more    
self-documenting and easier to understand. Some value classes in this package, like  
*Count* and *Bytes* have *toString()* values that are easier to read than a raw numeric  
value. For example, *Bytes* parses and converts to representations like "3.5 MB" or "500 bytes."

Values like *Count* can be helpful in making method parameters and return values clear,  
but at times the internal representation of a count will be a primitive value like *long* or  
*int* for the sake of efficiency.

### Counts <a name="counts"></a>

The *count* package holds discrete values representing some finite quantity:

* *BitCount* - A number of bits
* *Bytes* - A number of bytes
* *ConcurrentMutableCount* - A count whose value can be changed safely under multithreading
* *Count* - An arbitrary quantity that is always >= 0
* *Estimate* - A count that represents an estimate
* *LongRange* - A range of long values
* *Maximum* - A count that represents a maximum
* *Minimum* - A count that represents a minimum
* *MutableCount* - A count value that can be changed
* *Range* - A range of integer values

### Identifiers <a name="identifiers"></a>

Several classes are provided to create and define identifiers.

### Levels <a name="levels"></a>

In the same way that the *count* package defines discrete values, the *level* package defines  
floating-point values:

* *Level* - A value between 0 and 1
* *Percent* - An unbounded percentage
* *Priority* - A level used for establishing importance
* *Weight* - A weight factor for use in mathematical formulas
* *Confidence* - A value representing a level of confidence in something

### Mutable Values <a name="mutable-values"></a>

The *mutable* package defines classes for mutable values (which can be convenient when a  
lambda wants to change a local variable):

* *MutableIndex* - An integer index that can be changed
* *MutableInteger* - An integer value that can be changed
* *MutableLong* - A long value that can be changed
* *MutableValue* - An arbitrary value that can be changed

### Versions <a name="versions"></a>

The *Version* class represents a [*semantic version*](https://semver.org). Any object may provide a version by   
implementing *Versioned*, and a version can be associated with an object using *VersionedObject*.  
The [*kivakit-core-serialization*](../../../kivakit-core/serialization/README.md) project uses *VersionedObject*s to help ensure that all objects that  
are serialized have associated version information. This can help to ensure backwards  
compatibility with serialized data.

<br/>

![](../documentation/images/horizontal-line.png)
