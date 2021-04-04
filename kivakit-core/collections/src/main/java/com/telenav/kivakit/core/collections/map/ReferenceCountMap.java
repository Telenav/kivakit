////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.map;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramMap;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;

import java.util.HashMap;
import java.util.Map;

/**
 * A non-thread-safe map of reference counts.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
public class ReferenceCountMap<Key>
{
    private final Map<Key, MutableCount> referenceCount = new HashMap<>();

    /**
     * @return The reference count for the given object
     */
    public Count count(final Key object)
    {
        var count = referenceCount.get(object);
        if (count == null || count.isZero())
        {
            count = new MutableCount(1);
            referenceCount.put(object, count);
        }
        return count.asCount();
    }

    /**
     * Sets the count of the given object
     */
    public void count(final Key object, final Count count)
    {
        referenceCount.put(object, new MutableCount(count.asInt()));
    }

    /**
     * Decreases the reference count of the given object
     */
    public void dereference(final Key object)
    {
        referenceCount.get(object).decrement();
    }

    /**
     * @return True if the given object is referenced
     */
    public boolean isReferenced(final Key object)
    {
        return referenceCount.get(object).asLong() > 0L;
    }

    /**
     * Increases the reference count of the given object
     */
    public void reference(final Key key)
    {
        referenceCount.get(key).increment();
    }
}
