package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Frequency;

import java.time.Duration;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Converts to and from a frequency, expressed as a duration between cycles.
 *
 * @author jonathanl (shibo)
 * @see Duration
 */
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class FrequencyConverter extends BaseStringConverter<Frequency>
{
    /**
     * @param listener The listener to report problems to
     */
    public FrequencyConverter(Listener listener)
    {
        super(listener, Frequency::parseFrequency);
    }
}
