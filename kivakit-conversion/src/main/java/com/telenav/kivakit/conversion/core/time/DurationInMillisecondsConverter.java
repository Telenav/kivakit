package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.core.language.primitive.LongConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.Duration.milliseconds;

/**
 * Converts milliseconds to and from {@link Duration}
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class DurationInMillisecondsConverter extends BaseStringConverter<Duration>
{
    /** The underlying long value converter */
    private final LongConverter longConverter;

    /**
     * @param listener The listener to report problems to
     */
    public DurationInMillisecondsConverter(Listener listener)
    {
        super(listener, Duration.class);
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
                : milliseconds(milliseconds);
    }
}
