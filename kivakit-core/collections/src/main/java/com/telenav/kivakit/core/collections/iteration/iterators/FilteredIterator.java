////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.iteration.iterators;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramIteration;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

/**
 * An {@link Iterator} that wraps and filters the values of another {@link Iterator}.
 *
 * @author jonathanl (shibo)
 * @see BaseIterator
 * @see Filter
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class FilteredIterator<Element> extends BaseIterator<Element>
{
    private final Iterator<Element> iterator;

    private final Matcher<Element> filter;

    public FilteredIterator(final Iterator<Element> iterator, final Matcher<Element> filter)
    {
        this.iterator = iterator;
        this.filter = filter;
    }

    @Override
    protected Element onNext()
    {
        while (iterator.hasNext())
        {
            final var next = iterator.next();
            if (filter.matches(next))
            {
                return next;
            }
        }
        return null;
    }
}
