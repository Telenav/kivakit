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

package com.telenav.kivakit.collections.set.operations;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramSet;
import com.telenav.kivakit.collections.set.LogicalSet;
import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * The logical union of two sets. The two sets are combined without creating any new set.
 * <p>
 * This set is not modifiable. To change the union, you must modify the underlying set(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Subset<T> extends LogicalSet<T>
{
    private final Set<T> set;

    private final Matcher<T> matcher;

    public Subset(Set<T> set, Matcher<T> matcher)
    {
        this.set = set;
        this.matcher = matcher;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object object)
    {
        return set.contains(object) && matcher.matches((T) object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Set)
        {
            var that = (Set<T>) object;
            if (size() == that.size())
            {
                for (var value : this)
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
        for (var member : set)
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

    @Override
    public int size()
    {
        var size = 0;
        for (var member : set)
        {
            if (matcher.matches(member))
            {
                size++;
            }
        }
        return size;
    }
}
