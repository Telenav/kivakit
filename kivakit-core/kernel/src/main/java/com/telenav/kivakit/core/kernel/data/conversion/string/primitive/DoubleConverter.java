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

/**
 * Converts a double to and from a string.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionPrimitive.class)
public class DoubleConverter extends BaseStringConverter<Double>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public DoubleConverter(final Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Double onConvertToObject(final String value)
    {
        return Double.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected String onConvertToString(final Double value)
    {
        if (Double.isNaN(value) || Double.isInfinite(value))
        {
            return "N/A";
        }
        else
        {
            return value.toString();
        }
    }
}
