package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.core.time.Duration.parseDuration;

/**
 * Converts the given <code>String</code> to a new <code>Duration</code> object. The string can take the form of a
 * floating point number followed by a number of milliseconds, seconds, minutes, hours or days. For example "6 hours" or
 * "3.4 days". Parsing is case-insensitive.
 *
 * @author jonathanl (shibo)
 */
public class DurationConverter extends BaseStringConverter<Duration>
{
    public DurationConverter(Listener listener)
    {
        super(listener, value -> parseDuration(listener, value));
    }
}
