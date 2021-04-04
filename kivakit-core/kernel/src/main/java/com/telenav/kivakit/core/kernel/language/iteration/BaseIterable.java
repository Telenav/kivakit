////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.iteration;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.Iterator;

/**
 * Implements the {@link Iterable} interface by using a {@link Next} object to find the next value when iterating.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIteration.class)
public abstract class BaseIterable<T> implements Iterable<T>
{
    @Override
    public final Iterator<T> iterator()
    {
        return new BaseIterator<>()
        {
            private final Next<T> next = newNext();

            @Override
            protected T onNext()
            {
                return next.onNext();
            }
        };
    }

    /**
     * @return A new {@link Next} implementation for finding the next value in a sequence
     */
    @UmlRelation(label = "creates")
    protected abstract Next<T> newNext();
}
