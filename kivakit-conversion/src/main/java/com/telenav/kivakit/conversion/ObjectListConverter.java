package com.telenav.kivakit.conversion;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;

public class ObjectListConverter<Value> extends BaseStringConverter<ObjectList<Value>>
{
    private final String delimiter;

    protected ObjectListConverter(Listener listener, Class<ObjectList<Value>> toType, String delimiter)
    {
        super(listener, toType);
        this.delimiter = delimiter;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ObjectList<Value> onToValue(String value)
    {
        return (ObjectList<Value>) convertToList(value, delimiter);
    }
}
