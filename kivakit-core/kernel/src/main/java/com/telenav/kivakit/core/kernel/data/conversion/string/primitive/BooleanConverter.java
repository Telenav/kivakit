////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string.primitive;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.primitives.Booleans;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionPrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * Converts between booleans and strings. Several values in addition to "true" and "false" can be used to represent
 * truth and falsity, as defined in {@link Booleans#isTrue(String)} and {@link Booleans#isFalse(String)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionPrimitive.class)
public class BooleanConverter extends BaseStringConverter<Boolean>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public BooleanConverter(final Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Boolean onConvertToObject(final String value)
    {
        if (Booleans.isTrue(value))
        {
            return true;
        }
        if (Booleans.isFalse(value))
        {
            return false;
        }
        warning("Invalid boolean value $", value);
        return null;
    }
}
