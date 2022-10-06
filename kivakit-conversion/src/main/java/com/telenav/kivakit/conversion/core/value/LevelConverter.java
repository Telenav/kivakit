package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Converts to and from a {@link Level}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramConversionValue.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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
