package com.telenav.kivakit.kernel.data.conversion;

import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converter that converts between a {@link String} and any {@link Quantizable} value.
 *
 * @param <T> The type to convert to
 */
@LexakaiJavadoc(complete = true) public
class QuantizableConverter<T extends Quantizable> extends BaseStringConverter<T>
{
    private final MapFactory<Long, T> factory;

    public QuantizableConverter(Listener listener, MapFactory<Long, T> factory)
    {
        super(listener);
        this.factory = factory;
    }

    @Override
    protected T onToValue(String value)
    {
        return factory.newInstance(Long.parseLong(value));
    }
}
