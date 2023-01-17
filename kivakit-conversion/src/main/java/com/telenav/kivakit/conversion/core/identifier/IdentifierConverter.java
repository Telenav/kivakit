package com.telenav.kivakit.conversion.core.identifier;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.value.LongValuedConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.identifier.Identifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Converts to and from {@link Identifier}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionOther.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class IdentifierConverter extends LongValuedConverter<Identifier>
{
    /**
     * @param listener The listener to report problems to
     */
    public IdentifierConverter(Listener listener)
    {
        super(listener, Identifier.class, identifier -> identifier == null
            ? null
            : new Identifier(identifier));
    }

    public IdentifierConverter()
    {
        this(throwingListener());
    }
}
