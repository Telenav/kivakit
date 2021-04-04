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

/**
 * An iterator that has no values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class EmptyIterator<Element> implements Iterator<Element>
{
    @Override
    public boolean hasNext()
    {
        return false;
    }

    @Override
    public Element next()
    {
        return null;
    }

    @Override
    public void remove()
    {
    }
}
