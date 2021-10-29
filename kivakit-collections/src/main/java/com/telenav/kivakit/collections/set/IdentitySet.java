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

import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;

/**
 * A set of elements stored by identity (as opposed to using the {@link #hashCode()} / {@link #equals(Object)}
 * contract). Implemented with an {@link IdentityHashMap}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
@LexakaiJavadoc(complete = true)
public class IdentitySet<Element> extends AbstractSet<Element>
{
    private final IdentityHashMap<Element, Boolean> map = new IdentityHashMap<>();

    @Override
    public boolean add(Element element)
    {
        return map.put(element, true) != null;
    }

    @Override
    public boolean addAll(Collection<? extends Element> collection)
    {
        collection.forEach(this::add);
        return true;
    }

    @Override
    public void clear()
    {
        map.clear();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public boolean contains(Object object)
    {
        return map.containsKey((Element) object);
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public Iterator<Element> iterator()
    {
        return map.keySet().iterator();
    }

    @Override
    public boolean remove(Object element)
    {
        return map.remove(element);
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        collection.forEach(this::remove);
        return true;
    }

    @Override
    public int size()
    {
        return map.size();
    }
}
