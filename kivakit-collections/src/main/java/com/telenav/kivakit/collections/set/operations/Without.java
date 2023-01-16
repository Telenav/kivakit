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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramSet;
import com.telenav.kivakit.collections.set.LogicalSet;
import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A given set without the elements of another set (technically, the "relative complement"). The two sets are logically
 * combined without creating any new set.
 * <p>
 * This set is not modifiable. To change the set, you must modify the underlying set(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Without<T> extends LogicalSet<T>
{
    private final Set<T> set;

    private final Set<T> without;

    /**
     * @param set The base set
     * @param without The set whose elements should be excluded from the base set
     */
    public Without(Set<T> set, Set<T> without)
    {
        this.set = set;
        this.without = without;
    }

    @Override
    public boolean contains(Object object)
    {
        return set.contains(object) && !without.contains(object);
    }

    @Override
    public boolean isEmpty()
    {
        if (set.isEmpty())
        {
            return true;
        }
        for (var object : set)
        {
            if (!without.contains(object))
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
                    if (!without.contains(next))
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
        var size = set.size();
        for (var object : without)
        {
            if (set.contains(object))
            {
                size--;
            }
        }
        return size;
    }
}
