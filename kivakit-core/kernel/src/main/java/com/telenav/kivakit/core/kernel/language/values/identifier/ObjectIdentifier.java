////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.identifier;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Uses an object to efficiently provide ({@link #hashCode} and {@link #equals(Object)}) identity.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class ObjectIdentifier<T>
{
    private final T object;

    private final int hashCode;

    public ObjectIdentifier(final T object)
    {
        this.object = object;
        hashCode = object.hashCode();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof ObjectIdentifier)
        {
            final var that = (ObjectIdentifier<?>) object;
            return hashCode == that.hashCode && this.object.equals(that.object);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    public T object()
    {
        return object;
    }

    @Override
    public String toString()
    {
        return "[ObjectIdentifier object = " + object.toString() + "]";
    }
}
