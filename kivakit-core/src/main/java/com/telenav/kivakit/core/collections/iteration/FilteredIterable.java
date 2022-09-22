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
import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;

/**
 * An {@link Iterable} that wraps and filters the elements of another {@link Iterable} producing only elements that
 * match the {@link Matcher} given to the constructor.
 *
 * @author jonathanl (shibo)
 * @see BaseIterable
 * @see Filter
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = ApiStability.STABLE,
             testing = NONE,
             documentation = SUFFICIENT)
public class FilteredIterable<Element> extends BaseIterable<Element>
{
    private final Matcher<Element> filter;

    private final Iterable<Element> iterable;

    public FilteredIterable(Iterable<Element> iterable, Matcher<Element> filter)
    {
        this.iterable = iterable;
        this.filter = filter;
    }

    @Override
    protected NextValue<Element> newNext()
    {
        return new NextValue<>()
        {
            private final Iterator<Element> iterator = iterable.iterator();

            @Override
            public Element next()
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
        };
    }
}
