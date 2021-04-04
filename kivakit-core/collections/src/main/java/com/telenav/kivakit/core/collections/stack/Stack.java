////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.stack;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramStack;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

/**
 * An object list with {@link #push(Object)} and {@link #pop()}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramStack.class)
public class Stack<T> extends ObjectList<T>
{
    public Stack()
    {
    }

    public Stack(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * @return Pop the value off of the top of the stack
     */
    public T pop()
    {
        return isEmpty() ? null : removeLast();
    }

    /**
     * Push the given value onto the top of the stack
     */
    public void push(final T value)
    {
        add(value);
    }
}
