package com.telenav.kivakit.conversion.core.identifier;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.core.value.LongValuedConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.identifier.IntegerIdentifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converts to and from an {@link IntegerIdentifier}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionOther.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
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
}
