## kivakit-core-kernel - data.conversion &nbsp; ![](../../../documentation/images/convert-32.png)
![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.data.conversion*

### Index

[**Summary**](#summary)  
[**Example 1**](#example-1)  
[**Example 2**](#example-2)  
[**Example 3**](#example-3)

![](../documentation/images/horizontal-line.png)

### Summary

The data conversion package provides conversion between types, most particularly between object   
types and strings. Converters make use of the [**messaging**](messaging.md) framework to broadcast failure messages to  
interested listeners. In cases where there are nested converters, they may repeat failure messages from the  
sub-converter(s). Converters are also used throughout KivaKit by other mini-frameworks. For example:

>
>- [**kivakit-core-kernel**](../../kernel/README.md) uses string converters to convert between *ObjectList* and *StringList*
>- [**kivakit-core-commandline**](../../commandline/README.md) uses *StringConverter*s to parse command line arguments and switches
>- [**kivakit-core-configuration**](../../configuration) uses string converters to convert properties file information into objects
>- [**kivakit-core-resource**](../../resource/README.md) uses string converters to convert lines in text files into a sequence of objects
>- [**kivakit-serialization-json**](../../../kivakit-serialization/json/README.md) provides *gson* serialization via string converters
>- [**kivakit-data-formats-csv**](../../../kivakit-data/formats/csv/README.md) converts CSV columns into objects using string converters
>- [**kivakit-data-compression**](../../../kivakit-data/compression/README.md) saves codec objects in properties files using string converters
>

### Example 1

    // Create a converter which converts between strings and Colors enum values
    enum Colors { AMARANTH, OBSIDIAN, AZURE, FOSSIL, AQUA, LIME, TANGERINE }
    var converter = new EnumConverter(listener, Colors.class);

    // then convert some strings to enums,
    var colors = ObjectList.objectList("aqua", "lime", "tangerine");

    // and enums back to strings.
    System.out.println(colors.asStringList(converter));

### Example 2

    var converter = new Distance.Converter(listener);
    var sprint = converter.convert("100 meters");

### Example 3

    private static final Logger LOGGER = LoggerFactory.newLogger();

        [...]

    var converter = new IntegerConverter(LOGGER);
    var sprint = converter.convert("100 meters");
    var whoops = converter.convert("banana");

<br/>

![](../documentation/images/horizontal-line.png)
