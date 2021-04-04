package com.telenav.kivakit.core.collections.set;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramSet;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public class IdentitySet<Element> extends AbstractSet<Element>
{
    private final IdentityHashMap<Element, Boolean> map = new IdentityHashMap<>();

    @Override
    public boolean add(final Element element)
    {
        return map.put(element, true) != null;
    }

    @Override
    public boolean addAll(final Collection<? extends Element> collection)
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
    @SuppressWarnings({ "RedundantCast", "unchecked" })
    public boolean contains(final Object object)
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
    public boolean remove(final Object element)
    {
        return map.remove(element);
    }

    @Override
    public boolean removeAll(final Collection<?> collection)
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
