////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string.language;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts between {@link Class} objects and fully-qualified class names.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public class ClassConverter extends BaseStringConverter<Class<?>>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public ClassConverter(final Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> onConvertToObject(final String value)
    {
        try
        {
            return Class.forName(value);
        }
        catch (final ClassNotFoundException e)
        {
            throw new Problem(e, "Cannot find class ${debug}", value).asException();
        }
    }
}
