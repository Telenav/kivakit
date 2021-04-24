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

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.kivakit.core.collections.set.logical.LogicalSet;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.Set;

/**
 * The logical union of two sets. The two sets are combined without creating any new set.
 * <p>
 * This set is not modifiable. To change the union, you must modify the underlying set(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public class Subset<T> extends LogicalSet<T>
{
    private final Set<T> set;

    private final Matcher<T> matcher;

    public Subset(final Set<T> set, final Matcher<T> matcher)
    {
        this.set = set;
        this.matcher = matcher;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object object)
    {
        return set.contains(object) && matcher.matches((T) object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Set)
        {
            final var that = (Set<T>) object;
            if (size() == that.size())
            {
                for (final var value : this)
                {
                    if (!that.contains(value))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return set.hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        if (set.isEmpty())
        {
            return true;
        }
        for (final var member : set)
        {
            if (matcher.matches(member))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new BaseIterator<>()
        {
            private final Iterator<T> iterator = set.iterator();

            @Override
            protected T onNext()
            {
                while (iterator.hasNext())
                {
                    final var next = iterator.next();
                    if (matcher.matches(next))
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
        for (final var member : set)
        {
            if (matcher.matches(member))
            {
                size++;
            }
        }
        return size;
    }
}
