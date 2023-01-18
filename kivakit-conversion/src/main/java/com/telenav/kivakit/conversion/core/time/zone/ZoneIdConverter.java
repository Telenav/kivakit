package com.telenav.kivakit.conversion.core.time.zone;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import java.time.ZoneId;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

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

    public ZoneIdConverter()
    {
        this(throwingListener());
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
