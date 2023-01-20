package com.telenav.kivakit.conversion.core.time.epoch;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;

/**
 * Converts to and from {@link Time} using the given Java date time formatter
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class EpochTimeConverter extends BaseStringConverter<Time>
{
    /**
     * @param listener The listener to report problems to
     */
    public EpochTimeConverter(Listener listener)
    {
        super(listener, Time.class);
    }

    public EpochTimeConverter()
    {
        this(throwingListener());
    }

    @Override
    protected String onToString(Time time)
    {
        return Long.toString(time.epochMilliseconds());
    }

    @Override
    protected Time onToValue(String value)
    {
        return epochMilliseconds(value);
    }
}
