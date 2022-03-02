package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.project.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts to and from {@link Maximum}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramConversionValue.class)
public class MaximumConverter extends BaseStringConverter<Maximum>
{
    public MaximumConverter(Listener listener)
    {
        super(listener, Maximum::parseMaximum);
    }
}
