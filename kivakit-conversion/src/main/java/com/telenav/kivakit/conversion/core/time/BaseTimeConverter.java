package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.core.time.Time.epochMilliseconds;

/**
 * Converts to (unzoned) {@link Time} values
 *
 * @author jonathanl (shibo)
 */
public class BaseTimeConverter extends BaseStringConverter<Time>
{
    /** The underlying date/time formatter */
    private final DateTimeFormatter formatter;

    protected BaseTimeConverter(Listener listener,
                                DateTimeFormatter formatter)
    {
        super(listener, Time.class);
        this.formatter = formatter;
    }

    @Override
    protected String onToString(Time time)
    {
        var instant = Instant.ofEpochMilli(time.epochMilliseconds());
        return formatter.format(instant);
    }

    @Override
    protected Time onToValue(String value)
    {
        var parsed = formatter.parse(value);
        var time = Instant.from(parsed);
        return epochMilliseconds(time.toEpochMilli());
    }
}
