package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

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
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class BytesConverter extends BaseStringConverter<Bytes>
{
    public BytesConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected Bytes onToValue(String value)
    {
        return Bytes.parseBytes(this, value);
    }
}
