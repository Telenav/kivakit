package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converts to and from a {@link Percent}. Values can be like "5.2%"
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class PercentConverter extends BaseStringConverter<Percent>
{
    public PercentConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected Percent onToValue(String value)
    {
        return Percent.of(Double.parseDouble(value.endsWith("%")
                ? Strip.ending(value, "%")
                : value));
    }
}
