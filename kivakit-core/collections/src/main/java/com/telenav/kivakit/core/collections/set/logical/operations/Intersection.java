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

package com.telenav.kivakit.core.collections.set.logical.operations;

import com.telenav.kivakit.core.collections.set.logical.LogicalSet;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;

import java.util.Iterator;
import java.util.Set;

/**
 * The logical intersection of two sets. The two sets are combined without creating any new set.
 * <p>
 * This set is not modifiable. To change the intersection, you must modify the underlying set(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public class Intersection<T> extends LogicalSet<T>
{
    private final Set<T> larger;

    private final Set<T> smaller;

    public Intersection(final Set<T> a, final Set<T> b)
    {
        if (a.size() > b.size())
        {
            larger = a;
            smaller = b;
        }
        else
        {
            larger = b;
            smaller = a;
        }
    }

    @Override
    public boolean contains(final Object object)
    {
        return larger.contains(object) && smaller.contains(object);
    }

    @Override
    public boolean isEmpty()
    {
        for (final var object : smaller)
        {
            if (larger.contains(object))
            {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<T> iterator()
    {
        return new BaseIterator<>()
        {
            private final Iterator<T> smallerIterator = smaller.iterator();

            @Override
            protected T onNext()
            {
                while (smallerIterator.hasNext())
                {
                    final var next = smallerIterator.next();
                    if (larger.contains(next))
                    {
                        return next;
                    }
                }
                return null;
            }
        };
    }

    @Override
    public int size()
    {
        var size = 0;
        for (final var object : smaller)
        {
            if (larger.contains(object))
            {
                size++;
            }
        }
        return size;
    }
}
