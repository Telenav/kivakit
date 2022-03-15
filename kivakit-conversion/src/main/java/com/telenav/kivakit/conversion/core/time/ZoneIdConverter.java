package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import java.time.ZoneId;

public class ZoneIdConverter extends BaseStringConverter<ZoneId>
{
    /**
     * @param listener The conversion listener
     */
    public ZoneIdConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected ZoneId onToValue(String value)
    {
        return ZoneId.of(value);
    }
}
