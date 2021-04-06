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

package com.telenav.kivakit.core.collections.set.logical;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.kivakit.core.collections.set.logical.operations.Intersection;
import com.telenav.kivakit.core.collections.set.logical.operations.Union;
import com.telenav.kivakit.core.collections.set.logical.operations.Without;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.strings.Join;

import java.util.Collection;
import java.util.Set;

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
public abstract class LogicalSet<Element> implements Set<Element>
{
    @Override
    public boolean add(final Element element)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean addAll(final Collection<? extends Element> collection)
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
    public boolean containsAll(final Collection<?> collection)
    {
        for (final Object object : collection)
        {
            if (!contains(object))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(final Object o)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean removeAll(final Collection<?> c)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean retainAll(final Collection<?> c)
    {
        unsupported();
        return false;
    }

    @Override
    public Object[] toArray()
    {
        final var values = new Object[size()];
        var i = 0;
        for (final var value : this)
        {
            values[i++] = value;
        }
        return values;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <E> E[] toArray(final E[] array)
    {
        var i = 0;
        for (final var value : this)
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
