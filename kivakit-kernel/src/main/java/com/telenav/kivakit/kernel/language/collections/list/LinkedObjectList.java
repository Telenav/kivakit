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

package com.telenav.kivakit.kernel.language.collections.list;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.kernel.language.iteration.BaseIterator;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsList;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A linked list of objects that adds functionality to {@link LinkedList}, including the ability to serialize with Kryo
 * and to {@link #compress(CompressibleCollection.Method)} the collection to reduce consumed space (at which point it is
 * no longer a mutable linked list). Matching elements can be iterated with {@link #matching(Matcher)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsList.class)
@UmlExcludeSuperTypes
public class LinkedObjectList<T> extends AbstractSequentialList<T> implements CompressibleCollection
{
    private LinkedList<T> list = new LinkedList<>();

    private T[] array;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Method compress(Method method)
    {
        array = (T[]) list.toArray();
        list = null;
        return Method.FREEZE;
    }

    @Override
    public Method compressionMethod()
    {
        return array != null ? Method.FREEZE : Method.NONE;
    }

    @Override
    public T get(int index)
    {
        if (isCompressed())
        {
            return array[index];
        }
        else
        {
            return list.get(index);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCompressed()
    {
        return array != null;
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        if (isCompressed())
        {
            return new ListIterator<>()
            {
                int at = index;

                @Override
                public void add(T e)
                {
                    modify();
                    list.add(e);
                }

                @Override
                public boolean hasNext()
                {
                    return at < size();
                }

                @Override
                public boolean hasPrevious()
                {
                    return at > 0;
                }

                @Override
                public T next()
                {
                    return get(at++);
                }

                @Override
                public int nextIndex()
                {
                    return at + 1;
                }

                @Override
                public T previous()
                {
                    return get(at++);
                }

                @Override
                public int previousIndex()
                {
                    return at - 1;
                }

                @Override
                public void remove()
                {
                    modify();
                }

                @Override
                public void set(T e)
                {
                    modify();
                }
            };
        }
        else
        {
            return list.listIterator();
        }
    }

    public Iterator<T> matching(Matcher<T> matcher)
    {
        return new BaseIterator<>()
        {
            final ListIterator<T> iterator = listIterator();

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

    public Collection<T> remove(Matcher<T> matcher)
    {
        modify();
        List<T> removed = new ArrayList<>();
        var iterator = listIterator(0);
        while (iterator.hasNext())
        {
            var next = iterator.next();
            if (matcher.matches(next))
            {
                iterator.remove();
                removed.add(next);
            }
        }
        return removed;
    }

    @SuppressWarnings("SameReturnValue")
    public boolean replace(T object, T replacement)
    {
        modify();
        var iterator = listIterator(0);
        while (iterator.hasNext())
        {
            var next = iterator.next();
            if (next.equals(object))
            {
                iterator.set(replacement);
            }
        }
        return false;
    }

    @Override
    public int size()
    {
        if (isCompressed())
        {
            return array.length;
        }
        else
        {
            return list.size();
        }
    }

    private void modify()
    {
        if (isCompressed())
        {
            Ensure.fail("List is frozen");
        }
    }
}
