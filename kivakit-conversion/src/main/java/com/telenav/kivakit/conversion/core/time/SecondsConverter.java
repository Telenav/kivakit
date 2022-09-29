package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Converts seconds to and from {@link Duration}.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class SecondsConverter extends BaseStringConverter<Duration>
{
    /**
     * @param listener The listener to report problems to
     */
    public SecondsConverter(Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Duration onToValue(String value)
    {
        return Duration.seconds(Double.parseDouble(value));
    }
}
