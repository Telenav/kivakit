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

import com.telenav.kivakit.collections.iteration.iterators.CompoundIterator;
import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.iteration.Matching;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A set which contains any number of other sets by reference. The included sets cannot be directly modified via the
 * compound set itself, but the underlying sets can be altered (if they are mutable) on their own.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public class CompoundSet<Element> implements Set<Element>
{
    private final List<Set<Element>> sets = new ArrayList<>();

    public CompoundSet()
    {
    }

    @SafeVarargs
    public CompoundSet(final Set<Element> set, final Set<Element>... sets)
    {
        add(set);
        Collections.addAll(this.sets, sets);
    }

    public void add(final Set<Element> set)
    {
        sets.add(Collections.unmodifiableSet(set));
    }

    @Override
    public boolean add(final Element e)
    {
        return unsupported();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean addAll(final Collection<? extends Element> c)
    {
        return unsupported();
    }

    @Override
    public void clear()
    {
        unsupported();
    }

    @Override
    public boolean contains(final Object object)
    {
        for (final var set : sets)
        {
            if (set.contains(object))
            {
                return true;
            }
        }
        return false;
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
    public boolean isEmpty()
    {
        for (final var set : sets)
        {
            if (!set.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Element> iterator()
    {
        final var iterator = new CompoundIterator<Element>();
        for (final var set : sets)
        {
            iterator.add(set.iterator());
        }
        return iterator;
    }

    public Iterable<Element> matching(final Matcher<Element> matcher)
    {
        return new Matching<>(matcher)
        {
            @Override
            protected Iterator<Element> values()
            {
                return CompoundSet.this.iterator();
            }
        };
    }

    @Override
    public boolean remove(final Object o)
    {
        return unsupported();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean removeAll(final Collection<?> c)
    {
        return unsupported();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean retainAll(final Collection<?> c)
    {
        return unsupported();
    }

    @Override
    public int size()
    {
        var size = 0;
        for (final var set : sets)
        {
            size += set.size();
        }
        return size;
    }

    @Override
    public Object[] toArray()
    {
        return unsupported();
    }

    @Override
    public <E> E[] toArray(final E[] a)
    {
        return unsupported();
    }
}
