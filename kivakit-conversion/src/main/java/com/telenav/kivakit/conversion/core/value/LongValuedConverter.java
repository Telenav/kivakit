package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Converter that converts between a {@link String} and any {@link LongValued} value.
 *
 * @param <Value> The type to convert to
 */
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class LongValuedConverter<Value extends LongValued> extends BaseStringConverter<Value>
{
    /** The factory to create new values */
    private final MapFactory<Long, Value> factory;

    /**
     * @param listener The listener to call with problems
     * @param factory The factory to use to construct new values
     */
    public LongValuedConverter(Listener listener, MapFactory<Long, Value> factory)
    {
        super(listener);
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String onToString(Value value)
    {
        return Long.toString(value.longValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Value onToValue(String value)
    {
        return factory.newInstance(Long.parseLong(value));
    }
}
