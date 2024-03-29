package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.value.count.Bytes.parseBytes;

/**
 * Converts to and from {@link Bytes}. Valid suffixes are (case-insensitive):
 *
 * <ul>
 *     <li>b - bytes</li>
 *     <li>k - kilobytes</li>
 *     <li>m - megabytes</li>
 *     <li>g - gigabytes</li>
 *     <li>t - terabytes</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionValue.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class BytesConverter extends BaseStringConverter<Bytes>
{
    public BytesConverter(Listener listener)
    {
        super(listener, Bytes.class);
    }

    public BytesConverter()
    {
        this(throwingListener());
    }

    @Override
    protected Bytes onToValue(String value)
    {
        return parseBytes(this, value);
    }
}
