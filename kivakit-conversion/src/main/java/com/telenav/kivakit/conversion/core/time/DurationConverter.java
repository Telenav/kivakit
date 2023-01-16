package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.Duration.parseDuration;

/**
 * Converts the given <code>String</code> to a new <code>Duration</code> object. The string can take the form of a
 * floating point number followed by a number of milliseconds, seconds, minutes, hours or days. For example "6 hours" or
 * "3.4 days". Parsing is case-insensitive.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class DurationConverter extends BaseStringConverter<Duration>
{
    /**
     * @param listener The listener to report problems to
     */
    public DurationConverter(Listener listener)
    {
        super(listener, Duration.class, value -> parseDuration(listener, value));
    }
}
