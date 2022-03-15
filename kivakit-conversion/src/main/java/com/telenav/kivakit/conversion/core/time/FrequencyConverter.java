package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.time.Duration;

/**
 * Converts to and from a frequency, expressed as a duration between cycles.
 *
 * @author jonathanl (shibo)
 * @see Duration
 */
@LexakaiJavadoc(complete = true)
public class FrequencyConverter extends BaseStringConverter<Frequency>
{
    public FrequencyConverter(Listener listener)
    {
        super(listener, Frequency::parseFrequency);
    }
}
