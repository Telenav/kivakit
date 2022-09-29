package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Converts to and from {@link Minimum}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionValue.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class MinimumConverter extends BaseStringConverter<Minimum>
{
    public MinimumConverter(Listener listener)
    {
        super(listener, Minimum::parseMinimum);
    }
}
