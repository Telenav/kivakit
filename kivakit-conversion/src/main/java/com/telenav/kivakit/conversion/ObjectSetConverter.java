package com.telenav.kivakit.conversion;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;

public class ObjectSetConverter<Value> extends BaseStringConverter<ObjectSet<Value>>
{
    private final String delimiter;

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
