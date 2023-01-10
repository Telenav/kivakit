package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.Time.milliseconds;

/**
 * Converts to and from {@link Time} using the given Java date time formatter
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class TimeInMillisecondsConverter extends BaseStringConverter<Time>
{
    /**
     * @param listener The listener to report problems to
     */
    public TimeInMillisecondsConverter(Listener listener)
    {
        super(listener, Time.class);
    }

    @Override
    protected Time onToValue(String value)
    {
        return milliseconds(value);
    }
}
