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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIteration;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * An implementation of {@link Iterator} that takes care of the basic logic of an iterator. Subclasses only need to
 * implement {@link #onNext()} by returning the next value, or null if there is not any next value. The sequence of
 * objects returned can be filtered to only those objects matching a {@link Matcher} specified by
 * {@link #matching(Matcher)}.
 *
 * <p><b>Iteration</b></p>
 *
 * <ul>
 *     <li>{@link #onNext()} - Implemented by the subtype to return the next value, or null if there is none</li>
 * </ul>
 *
 * <p><b>Filtering</b></p>
 *
 * <ul>
 *     <li>{@link #matching(Matcher)} - Sets a matcher to be used to filter values during iteration</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseIterator<Value> implements Iterator<Value>
{
    /** The next value in the sequence, if any */
    private Value next;

    /** A filter to restrict values in the sequence */
    private Matcher<Value> matcher;

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns the matcher that must be satisfied for each object iterated
     */
    public Matcher<Value> matcher()
    {
        return matcher;
    }

    /**
     * @param matcher The filter to apply to this sequence
     */
    public BaseIterator<Value> matching(Matcher<Value> matcher)
    {
        this.matcher = matcher;
        return this;
    }

    @Override
    public final Value next()
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
        unsupported();
    }

    protected Value findNext()
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
            if (matcher == null || matcher.matches(next))
            {
                return next;
            }
        }
    }

    /**
     * Returns the next value in the sequence or null if there is none
     */
    protected abstract Value onNext();
}
