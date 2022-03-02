package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.project.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.value.count.Estimate;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts to and from {@link Estimate}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramConversionValue.class)
public class EstimateConverter extends BaseStringConverter<Estimate>
{
    public EstimateConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected Estimate onToValue(String value)
    {
        if (value.indexOf(',') > 0)
        {
            value = Strings.removeAll(value, ',');
        }
        return Estimate.estimate(Long.parseLong(value));
    }
}
