package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Converts to and from a {@link Percent}. Values can be like "5.2%"
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionValue.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class PercentConverter extends BaseStringConverter<Percent>
{
    public PercentConverter(Listener listener)
    {
        super(listener, Percent::parsePercent);
    }
}
