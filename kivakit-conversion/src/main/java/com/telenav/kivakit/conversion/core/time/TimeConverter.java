package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_DATE;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_DATE_TIME;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_TIME;

/**
 * Converts to and from {@link Time} using the given Java date time formatter
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class TimeConverter extends BaseStringConverter<Time>
{
    /**
     * Returns a converter that converts to/from kivakit date format: "yyyy.MM.dd"
     */
    public static TimeConverter kivakitDateConverter(Listener listener)
    {
        return new TimeConverter(listener, KIVAKIT_DATE);
    }

    /**
     * Returns a converter that converts to/from kivakit date/time format: yyyy.MM.dd_h.mma
     */
    public static TimeConverter kivakitDateTimeConverter(Listener listener)
    {
        return new TimeConverter(listener, KIVAKIT_DATE_TIME);
    }

    /**
     * Returns a converter that converts to/from kivakit time format: "h.mma"
     */
    public static TimeConverter kivakitTimeConverter(Listener listener)
    {
        return new TimeConverter(listener, KIVAKIT_TIME);
    }

    /** Java date/time formatter */
    private final DateTimeFormatter formatter;

    /**
     * @param listener The listener to report problems to
     * @param formatter The formatter to use
     */
    public TimeConverter(Listener listener, DateTimeFormatter formatter)
    {
        super(listener);
        this.formatter = formatter;
    }

    /**
     * {@inheritDoc}
     * @param dateTimeString The (guaranteed non-null, non-empty) value to convert
     * @return
     */
    @Override
    protected Time onToValue(String dateTimeString)
    {
        return Time.epochMilliseconds(Instant.from(formatter.parse(dateTimeString)).toEpochMilli());
    }
}