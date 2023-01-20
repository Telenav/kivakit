package com.telenav.kivakit.conversion;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

public class ObjectListConverter<Value> extends BaseStringConverter<ObjectList<Value>>
{
    private final String delimiter;

    public ObjectListConverter(Listener listener, Class<ObjectList<Value>> toType, String delimiter)
    {
        super(listener, toType);
        this.delimiter = delimiter;
    }

    public ObjectListConverter(Listener listener, Class<ObjectList<Value>> toType)
    {
        this(listener, toType, ",");
    }

    public ObjectListConverter(Class<ObjectList<Value>> toType, String delimiter)
    {
        this(throwingListener(), toType, delimiter);
    }

    public ObjectListConverter(Class<ObjectList<Value>> toType)
    {
        this(throwingListener(), toType, ",");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ObjectList<Value> onToValue(String value)
    {
        return (ObjectList<Value>) convertToList(value, delimiter);
    }
}
