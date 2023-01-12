package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.function.Mapper;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.lang.Long.parseLong;

/**
 * Converter that converts between a {@link String} and any {@link LongValued} value.
 *
 * @param <Value> The type to convert to
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class LongValuedConverter<Value extends LongValued> extends BaseStringConverter<Value>
{
    /** The factory to create new values */
    private final Mapper<Long, Value> factory;

    /**
     * @param listener The listener to call with problems
     * @param factory The factory to use to construct new values
     */
    public LongValuedConverter(Listener listener, Class<Value> type, Mapper<Long, Value> factory)
    {
        super(listener, type);
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
        return factory.map(parseLong(value));
    }
}
