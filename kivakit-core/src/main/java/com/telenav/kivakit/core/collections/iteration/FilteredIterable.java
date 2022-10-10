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
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.interfaces.collection.NextIterator;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * An {@link Iterable} that wraps and filters the elements of another {@link Iterable} producing only elements that
 * match the {@link Matcher} given to the constructor.
 *
 * @author jonathanl (shibo)
 * @see BaseIterable
 * @see Filter
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class FilteredIterable<Value> extends BaseIterable<Value>
{
    /** The matcher that must be satisfied for iterated objects */
    private final Matcher<Value> matcher;

    /** The iterable to filter */
    private final Iterable<Value> iterable;

    /**
     * @param iterable The iterable to filter
     * @param matcher The matcher that must be satisfied to include values in the iteration
     */
    public FilteredIterable(Iterable<Value> iterable, Matcher<Value> matcher)
    {
        this.iterable = iterable;
        this.matcher = matcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NextIterator<Value> newNextIterator()
    {
        return new NextIterator<>()
        {
            private final Iterator<Value> iterator = iterable.iterator();

            @Override
            public Value next()
            {
                while (iterator.hasNext())
                {
                    var next = iterator.next();
                    if (matcher.matches(next))
                    {
                        return next;
                    }
                }
                return null;
            }
        };
    }
}
