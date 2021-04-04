////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.collection;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceCollection;

import java.util.Iterator;

/**
 * An object, often a collection or related type, to which objects can be added. Provides default implementations for
 * adding values from objects that implement {@link Iterable} or {@link Iterator}. Note that all Java collections are
 * {@link Iterable}, so they can be added with {@link #addAll(Iterable)}
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceCollection.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface Addable<T>
{
    /**
     * Adds the given value
     *
     * @return True if the value was added
     */
    boolean add(T value);

    /**
     * @param values A sequence of values to add
     */
    default boolean addAll(final Iterable<? extends T> values)
    {
        for (final T value : values)
        {
            if (!add(value))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @param values A sequence of values to add
     */
    default boolean addAll(final Iterator<? extends T> values)
    {
        while (values.hasNext())
        {
            if (!add(values.next()))
            {
                return false;
            }
        }
        return true;
    }
}
