package com.telenav.kivakit.conversion;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

public class ObjectSetConverter<Value> extends BaseStringConverter<ObjectSet<Value>>
{
    private final String delimiter;

    public ObjectSetConverter(Class<ObjectSet<Value>> toType)
    {
        this(throwingListener(), toType, ",");
    }

    public ObjectSetConverter(Class<ObjectSet<Value>> toType, String delimiter)
    {
        this(throwingListener(), toType, delimiter);
    }

    protected ObjectSetConverter(Listener listener, Class<ObjectSet<Value>> toType)
    {
        this(listener, toType, ",");
    }

    protected ObjectSetConverter(Listener listener, Class<ObjectSet<Value>> toType, String delimiter)
    {
        super(listener, toType);
        this.delimiter = delimiter;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ObjectSet<Value> onToValue(String value)
    {
        return (ObjectSet<Value>) convertToSet(value, delimiter);
    }
}
