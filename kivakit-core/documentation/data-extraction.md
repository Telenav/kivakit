## kivakit-core-kernel data.extraction &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/bits-40.png)
![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.data.extraction*

### Index

[**Summary**](#summary)  
[**Extractors**](#extractors)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

The data extraction package provides a simple abstraction for extracting objects from some data source.

### Extractors <a name="extractors"></a>

The *Extractor* interface extracts a value type from a given object type. The interface extends *Broadcaster*,   
allowing extractors to report issues encountered during extraction.

    public interface Extractor<Value, From> extends Broadcaster
    {
        Value extract(From object);
    }

*BaseExtractor* provides some default functionality for *Extractor* implementations:

    public abstract class BaseExtractor<Value, From> extends BaseRepeater implements Extractor<Value, From>
    {
        protected BaseExtractor( Listener listener)
    
        public Value extract( From object)
    
        public ObjectList<Value> extract( From[] values)

        public ObjectList<Value> extractList( Map<String, String> map, final From key)
        public ObjectList<Value> extractList( Keyed<String, String> map, final From key)
        public ObjectList<Value> extractList( Map<String, String> map, final From key, final String separator)
        public ObjectList<Value> extractList( Keyed<String, String> map, final From key, final String separator)

        public abstract Value onExtract(From object);
    }

In addition to handling exceptions thrown by *onExtract(Object)*, *BaseExtractor* provides methods to extract  
lists of objects from maps or other *Keyed* objects.

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
