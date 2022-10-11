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
import com.telenav.kivakit.interfaces.collection.NextIterator;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Implements the {@link Iterable} interface by using a {@link NextIterator} object to find the next value when
 * iterating. The sequence of objects returned can be filtered to only those objects matching a {@link Matcher}
 * specified by {@link #matching(Matcher)}.
 *
 * <p><b>Iteration</b></p>
 *
 * <ul>
 *     <li>{@link #newNextIterator()} - Implemented by the subtype to return a {@link NextIterator}, which returns
 *     a sequence of values, terminated by a null value</li>
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
public abstract class BaseIterable<Value> implements Iterable<Value>
{
    /** A filter to restrict values in the sequence */
    private Matcher<Value> matcher = Matcher.matchAll();

    /**
     * {@inheritDoc}
     */
    @Override
    public final @NotNull Iterator<Value> iterator()
    {
        return new BaseIterator<>()
        {
            private final NextIterator<Value> next = newNextIterator();

            @Override
            protected Value onNext()
            {
                for (var at = next.next(); at != null; at = next.next())
                {
                    if (matcher.matches(at))
                    {
                        return at;
                    }
                }
                return null;
            }
        };
    }

    /**
     * Returns the matcher that must be satisfied for each object iterated
     */
    public Matcher<Value> matcher()
    {
        return matcher;
    }

    /**
     * @param matcher The matcher to apply to this sequence
     */
    public BaseIterable<Value> matching(Matcher<Value> matcher)
    {
        this.matcher = matcher;
        return this;
    }

    /**
     * Returns a new {@link NextIterator} implementation for finding the next value in a sequence
     */
    @UmlRelation(label = "creates")
    protected abstract NextIterator<Value> newNextIterator();
}
