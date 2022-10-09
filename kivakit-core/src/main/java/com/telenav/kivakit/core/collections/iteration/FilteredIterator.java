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
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * An {@link Iterator} that wraps and filters the values of another {@link Iterator}.
 *
 * @author jonathanl (shibo)
 * @see BaseIterator
 * @see Filter
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = STABILITY_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class FilteredIterator<Value> extends BaseIterator<Value>
{
    /** The iterator to filter */
    private final Iterator<Value> iterator;

    /** The matcher that must be satisfied for iterated objects */
    private final Matcher<Value> matcher;

    /**
     * @param iterator The iterator to filter
     * @param matcher The matcher that must be satisfied to include values in the iteration
     */
    public FilteredIterator(Iterator<Value> iterator, Matcher<Value> matcher)
    {
        this.iterator = iterator;
        this.matcher = matcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Value onNext()
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
}
