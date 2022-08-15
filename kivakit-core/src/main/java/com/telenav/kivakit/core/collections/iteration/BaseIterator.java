////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.iteration;

import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of {@link Iterator} that takes care of the basic logic of an iterator. Subclasses only need to
 * implement {@link #onNext()} by returning the next value, or null if there is not any next value.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public abstract class BaseIterator<T> implements Iterator<T>
{
    /** The next value in the sequence, if any */
    private T next;

    /** A filter to restrict values in the sequence */
    private Matcher<T> filter;

    /**
     * @return The match filter
     */
    public Matcher<T> filter()
    {
        return filter;
    }

    /**
     * @param filter The filter to apply to this sequence
     */
    public BaseIterator<T> filter(Matcher<T> filter)
    {
        this.filter = filter;
        return this;
    }

    @Override
    public final boolean hasNext()
    {
        // If there's not already a next value (the case where hasNext() is
        // called twice in a row), we get the next value
        if (next == null)
        {
            next = findNext();
        }
        return next != null;
    }

    @Override
    public final T next()
    {
        // If the next was already fetched by hasNext(), we return that. If
        // not, we call findNext() to locate the next value,
        var next = this.next != null ? this.next : findNext();

        // throw an exception if we're out of elements,
        if (next == null)
        {
            throw new NoSuchElementException("Out of objects");
        }

        // and null out the next value so that hasNext() has to
        // advance to the next value when it's called
        this.next = null;

        // then we return next element of the iteration
        return next;
    }

    @Override
    public void remove()
    {
        Ensure.unsupported();
    }

    protected T findNext()
    {
        while (true)
        {
            // Get the next value
            var next = onNext();

            // and return null if there is no next
            if (next == null)
            {
                return null;
            }

            // otherwise, return the element if there's no filter or the element matches the filter
            if (filter == null || filter.matches(next))
            {
                return next;
            }
        }
    }

    /**
     * @return The next value in the sequence or null if there is none
     */
    protected abstract T onNext();
}
