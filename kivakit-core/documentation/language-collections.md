# kivakit-core-kernel language.collections &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/set-40.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.collections*

### Index

[**Summary**](#summary)  
[**Lists**](#lists)  
[**Maps**](#maps)  
[**Sets**](#sets)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

The *kivakit-core-kernel* package '*language.collections*' contains implementations of three data structure types:   
*List*, *Set* and *Map*. An abstract base class is provided for each data structure (*BaseList*, *BaseSet* and *BaseMap*)  
and each of these base classes supports several interfaces from the [*interfaces*](interfaces.md) package in *kivakit-core-kernel*.  
In addition to the basic collection types provided by *kivakit-core-kernel*, the project *kivakit-core-collections* provides  
additional collections.

### Lists <a name="lists"></a>

[**Diagram of Package language.collections.list**](diagrams/com.telenav.kivakit.core.kernel.language.collections.list.svg)

The primary objects of interest in the *language.collections.list* package are *ObjectList* and *StringList*, which  
provide the following categories of functionality (in addition to base collection functionality):

#### ObjectList

* Mapping
* Sorting
* Copying
* Reversing
* Filtering
* Conversion
* Unique-ing
* Joining
* Searching

#### StringList

* Splitting
* Repeating
* Conversion
* Indenting
* Prefixing
* Quoting
* Maximum Length
* Numbering
* ASCII Art

### Maps <a name="maps"></a>

A number of *BaseMap* subclasses are provided by the *language.collections.map* package. More are available,  
including multi-maps (also known as list-maps) in the *kivakit-core-collections* project.

#### BaseMap Subclasses

[**Diagram of Package language.collections.map**](diagrams/com.telenav.kivakit.core.kernel.language.collections.map.svg)

* *ObjectMap* - Generic object map analogous to *ObjectList* or *ObjectSet*
* *ClassMap* - Maps from *Class* keys to *Value* objects
* *CountMap* - Maps from *Key* to *MutableCount*
* *NameMap* - Maps from *String* or *Name* to *NamedObject*
* *StringMap* - Maps from *String* to *Value*
* *StringToStringMap* - Maps from *String* to *String*
* *VariableMap* - Maps from *String* to *Value*, supporting variable expansion

#### PropertyMaps

The *kivakit-core-resources* extends *VariableMap* with *PropertyMap*, adding the ability to load data from  
*Resource*s (including *File*s, *InputStream*s and *PackageResource*s) and write data to *WritableResources*  
(including *Files* and *OutputStream*s). *PropertyMaps* and *VariableMaps* are used in various parts of the  
KivaKit to 'interpolate' values into a *String* (through the *VariableMap.expanded(String)* method). A good  
example of this is the 
[*MessageFormatter*](http://telenav-kivakit.mypna.com/8.0.6-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/MessageFormatter.html)
class which provides variable expansion for the *messaging*  
and *logging* frameworks.

#### System Information

Useful *PropertyMap*s are provided by several methods:

* *OperatingSystem.environmentVariables()* - OS environment variables
* *JavaVirtualMachine.properties()* - JVM system properties
* *JavaVirtualMachine.variables()* - OS environment variables *and* JVM system properties
* *Project.properties()* - OS environment variables, JVM system properties, Maven project  
  properties and build information
* *Application.properties()* - Easy access to *Project.properties()*.

#### Configuration and Settings

*PropertyMaps* are also used for storing configuration information and settings.
See [kivakit-core-configuration](../../configuration/README.md)  
for more details.

### Sets <a name="sets"></a>

[**Diagram of Package language.collections.set**](diagrams/com.telenav.kivakit.core.kernel.language.collections.set.svg)

Just as *ObjectList* subclasses *BaseList*, *ObjectSet* subclasses *BaseSet*. At this time it provides no additional   
functionality except that inherited from the abstract base class *BaseList*.

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
