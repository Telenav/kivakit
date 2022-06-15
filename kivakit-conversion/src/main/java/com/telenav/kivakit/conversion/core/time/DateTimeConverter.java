package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter extends BaseStringConverter<Time>
{
    private final DateTimeFormatter formatter;

    public DateTimeConverter(Listener listener, DateTimeFormatter formatter)
    {
        super(listener);
        this.formatter = formatter;
    }

    @Override
    protected Time onToValue(String dateTimeString)
    {
        return Time.epochMilliseconds(Instant.from(formatter.parse(dateTimeString)).toEpochMilli());
    }
}
