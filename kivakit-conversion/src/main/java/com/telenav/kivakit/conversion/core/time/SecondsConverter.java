package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static java.lang.Double.parseDouble;

/**
 * Converts seconds to and from {@link Duration}.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
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
        return seconds(parseDouble(value));
    }
}
