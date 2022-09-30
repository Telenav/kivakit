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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramSet;
import com.telenav.kivakit.collections.set.LogicalSet;
import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * The logical intersection of two sets. The two sets are combined without creating any new set.
 * <p>
 * This set is not modifiable. To change the intersection, you must modify the underlying set(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Intersection<T> extends LogicalSet<T>
{
    private final Set<T> larger;

    private final Set<T> smaller;

    public Intersection(Set<T> a, Set<T> b)
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
    public boolean contains(Object object)
    {
        return larger.contains(object) && smaller.contains(object);
    }

    @Override
    public boolean isEmpty()
    {
        for (var object : smaller)
        {
            if (larger.contains(object))
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
            private final Iterator<T> smallerIterator = smaller.iterator();

            @Override
            protected T onNext()
            {
                while (smallerIterator.hasNext())
                {
                    var next = smallerIterator.next();
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
        for (var object : smaller)
        {
            if (larger.contains(object))
            {
                size++;
            }
        }
        return size;
    }
}
