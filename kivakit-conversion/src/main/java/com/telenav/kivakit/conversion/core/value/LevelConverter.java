package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Converts to and from a {@link Level}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramConversionValue.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
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
