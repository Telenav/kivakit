# kivakit-core-kernel language.paths &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/footprints-40.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.paths*

### Index

[**Summary**](#summary)  
[**Path**](#path)  
[**StringPath**](#string-path)  
[**PackagePath**](#package-path)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

This package contains objects for expressing hierarchical paths such as package paths  
or filesystem paths. The *StringPath* object in this package is further extended by other  
projects in KivaKit to provide *ResourcePath*, *FilePath* and *NetworkPath*.

### Path <a name="path"></a>

The abstract *Path* object represents a hierarchical path of any *Comparable* element type:

    public abstract class Path<Element extends Comparable<Element>> implements
        Iterable<Element>,
        Comparable<Path<Element>>,
        Sized
    {
        [...]
    }

*Path* provides methods to:

* Handle absolute (rooted) and relative paths
* Retrieve path elements
* Create new paths using functional methods
* Check for prefixes and suffixes

### StringPath <a name="string-path"></a>

*StringPath* is a Path&lt;String&gt; which inherits all functionality in *Path* and adds functionality to:

* Join and split *String* paths using a separator
* Provide overrides to avoid type casting
* Contract long paths into a string of a maximum length

### PackagePath <a name="package-path"></a>

*PackagePath* is a *StringPath* which works with *Modules* and *ModuleResource* from the
[*core.kernel.language.modules*]() package to provide access to package resources.
*PackagePath* is used throughout KivaKit and is extended in [*kivakit-core-resource*](../../resource/README.md)
to provide *Package* and *PackageResource*, which make it even easier to locate and use package resources.

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
