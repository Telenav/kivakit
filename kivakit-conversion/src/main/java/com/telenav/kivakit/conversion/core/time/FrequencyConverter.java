package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Frequency;

import java.time.Duration;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Converts to and from a frequency, expressed as a duration between cycles.
 *
 * @author jonathanl (shibo)
 * @see Duration
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
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
