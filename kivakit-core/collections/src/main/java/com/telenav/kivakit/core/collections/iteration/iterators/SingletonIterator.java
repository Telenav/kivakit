////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.iteration.iterators;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * An {@link Iterator} implementation with only one value to iterate.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class SingletonIterator<Element> implements Iterator<Element>
{
    private final Element element;

    private boolean iterated;

    public SingletonIterator(final Element element)
    {
        this.element = element;
    }

    @Override
    public boolean hasNext()
    {
        return !iterated;
    }

    @Override
    public Element next()
    {
        if (!iterated)
        {
            iterated = true;
            return element;
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove()
    {
        unsupported();
    }
}
