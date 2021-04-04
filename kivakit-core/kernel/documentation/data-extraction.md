## kivakit-core-kernel data.extraction &nbsp; ![](../../../documentation/images/bits-40.png)
![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.data.extraction*

### Index

[**Summary**](#summary)  
[**Extractors**](#extractors)

![](../documentation/images/horizontal-line.png)

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
        protected BaseExtractor(final Listener listener)
    
        public Value extract(final From object)
    
        public ObjectList<Value> extract(final From[] values)

        public ObjectList<Value> extractList(final Map<String, String> map, final From key)
        public ObjectList<Value> extractList(final Keyed<String, String> map, final From key)
        public ObjectList<Value> extractList(final Map<String, String> map, final From key, final String separator)
        public ObjectList<Value> extractList(final Keyed<String, String> map, final From key, final String separator)

        public abstract Value onExtract(From object);
    }

In addition to handling exceptions thrown by *onExtract(Object)*, *BaseExtractor* provides methods to extract  
lists of objects from maps or other *Keyed* objects.

<br/>

![](../documentation/images/horizontal-line.png)
