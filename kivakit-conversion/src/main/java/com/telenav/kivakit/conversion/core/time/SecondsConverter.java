package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Converts seconds to and from {@link Duration}.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
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
        return Duration.seconds(Double.parseDouble(value));
    }
}
