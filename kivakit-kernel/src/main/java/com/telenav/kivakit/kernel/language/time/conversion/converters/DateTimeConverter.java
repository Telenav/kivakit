package com.telenav.kivakit.kernel.language.time.conversion.converters;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.messaging.Listener;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter extends BaseStringConverter<Time>
{
    private final DateTimeFormatter formatter;

    public DateTimeConverter(final Listener listener, DateTimeFormatter formatter)
    {
        super(listener);
        this.formatter = formatter;
    }

    @Override
    protected Time onToValue(final String dateTimeString)
    {
        return Time.milliseconds(Instant.from(formatter.parse(dateTimeString)).toEpochMilli());
    }
}
