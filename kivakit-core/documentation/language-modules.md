# kivakit-core-kernel language.modules &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/stars-48.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.modules*

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary

This package contains classes for working with Java 9+ modules.

### Modules

The *Modules* class performs module scanning to discover *ModuleResource*s. *A ModuleResource*  
is not a *kivakit-core-resource* *Resource*, but rather a simple container of information that can be used  
by *PackageResource* in that project to provide access to package resources located within modules.  
The key properties that *ModuleResource* provides are:

* *javaPath()* - The *java.nio.file.Path* to the resource
* *lastModified()* - The last-modified time of the resource
* *packagePath()* - The *PackagePath* to the resource
* *size()* - The size of the resource
* *uri()* - The location of the resource, so it can be loaded

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
