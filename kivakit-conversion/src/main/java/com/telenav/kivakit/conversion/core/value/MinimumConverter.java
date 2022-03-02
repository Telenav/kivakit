package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converts to and from {@link Minimum}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class MinimumConverter extends BaseStringConverter<Minimum>
{
    public MinimumConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected Minimum onToValue(String value)
    {
        return Minimum.parseMinimum(this, value);
    }
}
