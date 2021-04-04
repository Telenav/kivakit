////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string.primitive;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionPrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.core.kernel.data.conversion.string.primitive.HexadecimalLongConverter.Style.JAVA;

/**
 * Converts between hexadecimal strings and long values. The constructor parameter {@link Style} allows conversion of
 * {@link Style#JAVA} (0x prefixed), {@link Style#CSS} (# prefixed) and {@link Style#NONE} (no prefix) values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionPrimitive.class)
public class HexadecimalLongConverter extends BaseStringConverter<Long>
{
    public enum Style
    {
        NONE(""),
        JAVA("0x"),
        CSS("#");

        private final String prefix;

        Style(final String prefix)
        {
            this.prefix = prefix;
        }

        @Override
        public String toString()
        {
            return prefix;
        }
    }

    private final Style style;

    /**
     * @param listener The listener to hear any conversion issues
     */
    public HexadecimalLongConverter(final Listener listener)
    {
        this(listener, JAVA);
    }

    /**
     * @param listener The listener to hear any conversion issues
     */
    public HexadecimalLongConverter(final Listener listener, final Style style)
    {
        super(listener);
        this.style = style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Long onConvertToObject(final String value)
    {
        if (value.startsWith(style.prefix))
        {
            return Long.parseLong(value.substring(style.prefix.length()), 16);
        }
        else
        {
            problem("Hexadecimal long does not start with '" + style + "': ${debug}", value);
            return null;
        }
    }
}
