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

package com.telenav.kivakit.collections.set;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramSet;
import com.telenav.kivakit.collections.set.operations.Intersection;
import com.telenav.kivakit.collections.set.operations.Union;
import com.telenav.kivakit.collections.set.operations.Without;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.string.Join;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Base classes for sets that represent an immutable logical view of two or more sets, combined with some sort of
 * logical operation. Logical sets are not modifiable. To change the contents of a logical set, you must modify the
 * underlying sets that are being logically combined.
 *
 * @author jonathanl (shibo)
 * @see Union
 * @see Intersection
 * @see Without
 */
@UmlClassDiagram(diagram = DiagramSet.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class LogicalSet<Element> implements Set<Element>
{
    @Override
    public boolean add(Element element)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Element> collection)
    {
        unsupported();
        return false;
    }

    @Override
    public void clear()
    {
        unsupported();
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        for (Object object : collection)
        {
            if (!contains(object))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Object o)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c)
    {
        unsupported();
        return false;
    }

    @Override
    public Object[] toArray()
    {
        var values = new Object[size()];
        var i = 0;
        for (var value : this)
        {
            values[i++] = value;
        }
        return values;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <E> E[] toArray(E @NotNull [] array)
    {
        var i = 0;
        for (var value : this)
        {
            array[i++] = (E) value;
        }
        return array;
    }

    @Override
    public String toString()
    {
        return Join.join(this, ", ");
    }

    private void unsupported()
    {
        Ensure.unsupported("A logical set is immutable through the Set interface. To modify a logical set (union, intersection or without), you must change the underlying set(s)");
    }
}
