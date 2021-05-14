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

package com.telenav.kivakit.collections.set;

import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A convenient implementation of {@link Set} using {@link ConcurrentHashMap}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
@LexakaiJavadoc(complete = true)
public class ConcurrentHashSet<Element> implements Set<Element>, Serializable
{
    private final ConcurrentHashMap<Element, Element> members;

    public ConcurrentHashSet()
    {
        members = new ConcurrentHashMap<>();
    }

    public ConcurrentHashSet(final int initialCapacity, final float loadFactor)
    {
        members = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    @Override
    public boolean add(final Element element)
    {
        return members.put(element, element) != null;
    }

    @Override
    public boolean addAll(final Collection<? extends Element> collection)
    {
        var changed = false;
        for (final Element object : collection)
        {
            changed = add(object) || changed;
        }
        return changed;
    }

    @Override
    public void clear()
    {
        members.clear();
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(final Object object)
    {
        return members.get(object) != null;
    }

    public synchronized boolean containsAdd(final Element object)
    {
        final var contains = contains(object);
        add(object);
        return contains;
    }

    @Override
    public boolean containsAll(final @NotNull Collection<?> collection)
    {
        return members.keySet().containsAll(collection);
    }

    /**
     * Gets the value currently in the set is equal to the given prototype object
     *
     * @param prototype The object to match against
     * @return Any object in the current set that matches the given object
     */
    public Element get(final Element prototype)
    {
        return members.get(prototype);
    }

    @Override
    public boolean isEmpty()
    {
        return members.isEmpty();
    }

    @Override
    public @NotNull Iterator<Element> iterator()
    {
        return members.keySet().iterator();
    }

    @Override
    public boolean remove(final Object key)
    {
        return members.remove(key) != null;
    }

    @Override
    public boolean removeAll(final Collection<?> collection)
    {
        var changed = false;
        for (final Object object : collection)
        {
            changed = remove(object) || changed;
        }
        return changed;
    }

    @Override
    public boolean retainAll(final @NotNull Collection<?> c)
    {
        return unsupported();
    }

    @Override
    public int size()
    {
        return members.size();
    }

    /**
     * Takes any object matching the given prototype out of the set, returning the set's value (but not necessarily the
     * prototype).
     *
     * @param prototype The object to match against
     * @return Any matching object after removing it from the set
     */
    public Element take(final Element prototype)
    {
        return members.remove(prototype);
    }

    @Override
    public Object[] toArray()
    {
        return members.keySet().toArray();
    }

    @SuppressWarnings({ "SuspiciousToArrayCall" })
    @Override
    public <E> E[] toArray(final E[] array)
    {
        return members.keySet().toArray(array);
    }
}
