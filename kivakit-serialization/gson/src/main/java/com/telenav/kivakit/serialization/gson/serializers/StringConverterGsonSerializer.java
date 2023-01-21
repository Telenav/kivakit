package com.telenav.kivakit.serialization.gson.serializers;

import com.google.gson.Gson;
import com.telenav.kivakit.conversion.StringConverter;

/**
 * A base class for {@link Gson} serializers that convert from
 */
public class StringConverterGsonSerializer<V> extends BaseGsonStringSerializer<V>
{
    private final StringConverter<V> converter;

    public StringConverterGsonSerializer(StringConverter<V> converter)
    {
        super(converter.toType());
        this.converter = converter;
    }

    @Override
    protected final V onDeserialize(String serialized)
    {
        return converter.convert(serialized);
    }

    @Override
    protected final String onSerialize(V value)
    {
        return converter.unconvert(value);
    }
}
