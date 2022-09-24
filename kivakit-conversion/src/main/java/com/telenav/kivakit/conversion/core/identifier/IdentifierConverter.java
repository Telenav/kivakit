package com.telenav.kivakit.conversion.core.identifier;

import com.telenav.kivakit.conversion.core.value.LongValuedConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.value.identifier.Identifier;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts to and from {@link Identifier}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramConversionOther.class)
public class IdentifierConverter extends LongValuedConverter<Identifier>
{
    public IdentifierConverter(Listener listener)
    {
        super(listener, identifier -> identifier == null
                ? null
                : new Identifier(identifier));
    }
}
