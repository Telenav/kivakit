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

import com.telenav.kivakit.annotations.code.ApiStability;
import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * An {@link Iterator} that wraps and filters the values of another {@link Iterator}.
 *
 * @author jonathanl (shibo)
 * @see BaseIterator
 * @see Filter
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@ApiQuality(stability = ApiStability.STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class FilteredIterator<Element> extends BaseIterator<Element>
{
    private final Iterator<Element> iterator;

    private final Matcher<Element> filter;

    public FilteredIterator(Iterator<Element> iterator, Matcher<Element> filter)
    {
        this.iterator = iterator;
        this.filter = filter;
    }

    @Override
    protected Element onNext()
    {
        while (iterator.hasNext())
        {
            var next = iterator.next();
            if (filter.matches(next))
            {
                return next;
            }
        }
        return null;
    }
}
