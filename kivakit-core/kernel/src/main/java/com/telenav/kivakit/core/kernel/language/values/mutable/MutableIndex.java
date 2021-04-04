////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.mutable;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * A mutable index value for use in lambdas and anonymous inner classes. Can be {@link #increment()}ed, {@link
 * #decrement()}ed, added to with {@link #add(int)}, set with {@link #index(int)} and retrieved with {@link #get()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class MutableIndex
{
    private int index;

    public MutableIndex()
    {
    }

    public MutableIndex(final int index)
    {
        if (index < 0)
        {
            Ensure.fail("Negative count ", index);
        }
        this.index = index;
    }

    @SuppressWarnings("UnusedReturnValue")
    public int add(final int that)
    {
        return index + that;
    }

    public int decrement()
    {
        return index--;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof MutableIndex)
        {
            final var that = (MutableIndex) object;
            return index == that.index;
        }
        return false;
    }

    public int get()
    {
        return index;
    }

    @Override
    public int hashCode()
    {
        return Integer.hashCode(index);
    }

    public int increment()
    {
        return index++;
    }

    public void index(final int index)
    {
        this.index = index;
    }

    public void set(final int index)
    {
        this.index = index;
    }

    @Override
    public String toString()
    {
        return Count.count(index).toCommaSeparatedString();
    }
}
