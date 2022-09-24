package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converter that converts between a {@link String} and any {@link LongValued} value.
 *
 * @param <Value> The type to convert to
 */
@LexakaiJavadoc(complete = true)
public class LongValuedConverter<Value extends LongValued> extends BaseStringConverter<Value>
{
    private final MapFactory<Long, Value> factory;

    public LongValuedConverter(Listener listener, MapFactory<Long, Value> factory)
    {
        super(listener);
        this.factory = factory;
    }

    @Override
    protected String onToString(Value value)
    {
        return Long.toString(value.longValue());
    }

    @Override
    protected Value onToValue(String value)
    {
        return factory.newInstance(Long.parseLong(value));
    }
}
