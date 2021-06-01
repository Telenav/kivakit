package com.telenav.kivakit.kernel.language.time.conversion.converters;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.messaging.Listener;

import java.time.ZoneId;

public class ZoneIdConverter extends BaseStringConverter<ZoneId>
{
    /**
     * @param listener The conversion listener
     */
    public ZoneIdConverter(final Listener listener)
    {
        super(listener);
    }

    @Override
    protected ZoneId onConvertToObject(final String value)
    {
        return ZoneId.of(value);
    }
}
