package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.value.level.Level.parseLevel;

/**
 * Converts to and from a {@link Level}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramConversionValue.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LevelConverter extends BaseStringConverter<Level>
{
    private boolean lenient;

    public LevelConverter(Listener listener)
    {
        super(listener, Level.class);
    }

    public LevelConverter()
    {
        this(throwingListener());
    }

    public void lenient(boolean lenient)
    {
        this.lenient = lenient;
    }

    @Override
    protected Level onToValue(String value)
    {
        return parseLevel(this, value, lenient);
    }
}
