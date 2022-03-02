package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.project.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts to and from a {@link Level}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramConversionValue.class)
public class LevelConverter extends BaseStringConverter<Level>
{
    private boolean lenient;

    public LevelConverter(Listener listener)
    {
        super(listener);
    }

    public void lenient(boolean lenient)
    {
        this.lenient = lenient;
    }

    @Override
    protected Level onToValue(String value)
    {
        return Level.parseLevel(this, value, lenient);
    }
}
