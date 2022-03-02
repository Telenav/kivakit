package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.core.language.primitive.LongConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converts milliseconds to and from {@link Duration}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class MillisecondsConverter extends BaseStringConverter<Duration>
{
    private final LongConverter longConverter;

    public MillisecondsConverter(Listener listener)
    {
        super(listener);
        longConverter = new LongConverter(listener);
    }

    @Override
    protected Duration onToValue(String value)
    {
        var milliseconds = longConverter.convert(value);
        return milliseconds == null
                ? null
                : Duration.milliseconds(milliseconds);
    }
}
