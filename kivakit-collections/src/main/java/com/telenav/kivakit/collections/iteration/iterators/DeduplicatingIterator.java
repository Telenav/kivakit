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

package com.telenav.kivakit.collections.iteration.iterators;

import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramIteration;
import com.telenav.kivakit.kernel.language.iteration.BaseIterator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class implements an iterator which can deduplicate the objects returned by the wrapped iterator. Each element
 * returned by the wrapped iterator is kept in a {@link Set} and only values that have not been seen before are produced
 * as part of the sequence for the caller. The iterated values must correctly implement the {@link #hashCode()} / {@link
 * #equals(Object)} contract.
 *
 * @author Junwei
 * @author jonathanl (shibo)
 * @version 1.0.0 2012-8-27
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class DeduplicatingIterator<Element> extends BaseIterator<Element>
{
    private final Iterator<Element> iterator;

    private final Set<Element> existing = new HashSet<>();

    public DeduplicatingIterator(final Iterator<Element> iterator)
    {
        this.iterator = iterator;
    }

    @Override
    protected Element onNext()
    {
        if (iterator.hasNext())
        {
            final var next = iterator.next();
            if (!existing.contains(next))
            {
                existing.add(next);
                return next;
            }
            else
            {
                return onNext();
            }
        }
        else
        {
            return null;
        }
    }
}
