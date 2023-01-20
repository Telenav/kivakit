package com.telenav.kivakit.conversion.core.identifier;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.value.LongValuedConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.identifier.IntegerIdentifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Converts to and from an {@link IntegerIdentifier}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionOther.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class IntegerIdentifierConverter extends LongValuedConverter<IntegerIdentifier>
{
    /**
     * @param listener The listener to report problems to
     */
    public IntegerIdentifierConverter(Listener listener)
    {
        super(listener, IntegerIdentifier.class, identifier -> identifier == null
            ? null
            : new IntegerIdentifier(identifier.intValue()));
    }

    public IntegerIdentifierConverter()
    {
        this(throwingListener());
    }
}
