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
 * An object, often a collection or related type, to which objects can be appended. Provides default implementations for
 * appending values from objects that implement {@link Iterable} or {@link Iterator}. Note that all Java collections are
 * {@link Iterable}, so they can be appended with {@link #appendAll(Iterable)}
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceCollection.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface Appendable<T>
{
    /**
     * Adds the given value
     *
     * @return Self reference for chaining of append calls
     */
    Appendable<T> append(T value);

    /**
     * @param values A sequence of values to add
     */
    default Appendable<T> appendAll(final Iterator<? extends T> values)
    {
        while (values.hasNext())
        {
            append(values.next());
        }
        return this;
    }

    /**
     * @param values A sequence of values to add
     */
    default Appendable<T> appendAll(final Iterable<? extends T> values)
    {
        for (final T value : values)
        {
            append(value);
        }
        return this;
    }
}
