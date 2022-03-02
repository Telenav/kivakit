package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.project.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts to and from a {@link Count}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramConversionValue.class)
public class CountConverter extends BaseStringConverter<Count>
{
    public CountConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected Count onToValue(String value)
    {
        var count = Longs.parseFast(value, -1);
        return count < 0 ? null : Count.count(count);
    }
}
