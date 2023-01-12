package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converts to and from a {@link Percent}. Values can be like "5.2%"
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionValue.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class PercentConverter extends BaseStringConverter<Percent>
{
    public PercentConverter(Listener listener)
    {
        super(listener, Percent.class, Percent::parsePercent);
    }
}
