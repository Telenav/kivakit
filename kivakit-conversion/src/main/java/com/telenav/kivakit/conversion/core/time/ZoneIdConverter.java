package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import java.time.ZoneId;

/**
 * Converts to and from zone id objects
 *
 * @author jonathanl (shibo)
 */
public class ZoneIdConverter extends BaseStringConverter<ZoneId>
{
    /**
     * @param listener The conversion listener
     */
    public ZoneIdConverter(Listener listener)
    {
        super(listener, ZoneId.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ZoneId onToValue(String value)
    {
        return ZoneId.of(value);
    }
}
