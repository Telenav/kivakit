package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;
import static com.telenav.kivakit.core.time.TimeZones.utc;

/**
 * Converts to and from {@link Time} using the given Java date time formatter
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class TimeConverter extends BaseStringConverter<Time>
{
    /** Java date/time formatter */
    private final DateTimeFormatter formatter;

    /**
     * @param listener The listener to report problems to
     * @param formatter The formatter to use
     */
    public TimeConverter(Listener listener, DateTimeFormatter formatter)
    {
        super(listener, Time.class);
        this.formatter = formatter;
    }

    @Override
    public String onToString(Time time)
    {
        return formatter.format(time.asJavaInstant());
    }

    /**
     * {@inheritDoc}
     *
     * @param dateTimeString The (guaranteed non-null, non-empty) value to convert
     */
    @Override
    protected Time onToValue(String dateTimeString)
    {
        var parsed = formatter.parse(dateTimeString, LocalDateTime::from);
        var utc = parsed.atZone(utc());
        return epochMilliseconds(utc.toInstant().toEpochMilli());
    }
}
