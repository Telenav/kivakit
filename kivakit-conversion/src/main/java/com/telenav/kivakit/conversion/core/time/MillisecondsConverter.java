package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.core.language.primitive.LongConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Converts milliseconds to and from {@link Duration}
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MillisecondsConverter extends BaseStringConverter<Duration>
{
    /** The underlying long value converter */
    private final LongConverter longConverter;

    /**
     * @param listener The listener to report problems to
     */
    public MillisecondsConverter(Listener listener)
    {
        super(listener);
        longConverter = new LongConverter(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Duration onToValue(String value)
    {
        var milliseconds = longConverter.convert(value);
        return milliseconds == null
                ? null
                : Duration.milliseconds(milliseconds);
    }
}
