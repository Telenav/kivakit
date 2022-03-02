package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converts seconds to and from {@link Duration}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class SecondsConverter extends BaseStringConverter<Duration>
{
    public SecondsConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected Duration onToValue(String value)
    {
        var seconds = Double.parseDouble(value);
        if (seconds >= 0)
        {
            return Duration.seconds(seconds);
        }
        return null;
    }
}
