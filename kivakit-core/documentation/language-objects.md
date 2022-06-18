# kivakit-core-kernel language.objects &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/set-40.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.objects*

### Index

[**Summary**](#summary)  
[**Object Utilities**](#object-utilities)  
[**Lazy Initialization**](#lazy-initialization)  
[**Pairs**](#pairs)  
[**References**](#references)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

This package contains helpful classes for working with Java objects.

### Object Utilities <a name="summary"></a>

The *Objects* and *Hash* classes provide some useful utility methods that are generic to all  
Java objects. *Hash* provides hash codes for objects, including [*Knuth*](https://en.wikipedia.org/wiki/Donald_Knuth) hash codes. The  
*Objects* class provides a few simple methods that make basic object operations like equality  
more concise or less error prone.

### Lazy Initialization <a name="summary"></a>

The *Lazy* class allows for delayed object construction. This can be especially useful when  
there are issues with the ordering of Java class or object initialization. Usage of *Lazy* looks  
like this:

    private static final Lazy<LocalHost> LOCALHOST = Lazy.of(LocalHost::new);

        [...]

    var localhost = LOCALHOST.get();

*MappedLazy* provides the same functionality except that it accepts a *MapFactory* as its  
constructor parameter instead of a *Factory*. This allows construction of objects to be  
parameterized.

### Pairs <a name="summary"></a>

The *Pair* class associates two objects as a pair. This is sometimes known as a "2-tuple".

### References <a name="summary"></a>

The references sub-package provides some classes for managing references to allow objects  
that have not been recently used to be "softened" (i.e., demoted from a normal reference to a  
[*SoftReference*](https://docs.oracle.com/javase/7/docs/api/java/lang/ref/SoftReference.html) or
[*WeakReference*](https://docs.oracle.com/javase/8/docs/api/java/lang/ref/WeakReference.html)), allowing such objects to be collected.

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
