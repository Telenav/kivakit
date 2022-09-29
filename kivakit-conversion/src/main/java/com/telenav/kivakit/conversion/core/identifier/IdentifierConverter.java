package com.telenav.kivakit.conversion.core.identifier;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.core.value.LongValuedConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.identifier.Identifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Converts to and from {@link Identifier}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionOther.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class IdentifierConverter extends LongValuedConverter<Identifier>
{
    /**
     * @param listener The listener to report problems to
     */
    public IdentifierConverter(Listener listener)
    {
        super(listener, identifier -> identifier == null
                ? null
                : new Identifier(identifier));
    }
}
