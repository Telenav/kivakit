package com.telenav.kivakit.conversion.core.time.frequency;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Frequency;

import java.time.Duration;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Converts to and from a frequency, expressed as a duration between cycles.
 *
 * @author jonathanl (shibo)
 * @see Duration
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FrequencyConverter extends BaseStringConverter<Frequency>
{
    /**
     * @param listener The listener to report problems to
     */
    public FrequencyConverter(Listener listener)
    {
        super(listener, Frequency.class, Frequency::parseFrequency);
    }

    public FrequencyConverter()
    {
        this(throwingListener());
    }

}
