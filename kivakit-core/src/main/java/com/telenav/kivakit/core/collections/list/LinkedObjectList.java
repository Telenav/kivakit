package com.telenav.kivakit.core.collections.list;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.value.count.Maximum;

import java.util.Collection;
import java.util.LinkedList;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;

/**
 * An {@link ObjectList} backed by a {@link LinkedList}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public class LinkedObjectList<Value> extends ObjectList<Value>
{
    public LinkedObjectList()
    {
        super(new LinkedList<>());
    }

    public LinkedObjectList(Maximum maximumSize)
    {
        super(maximumSize);
    }

    public LinkedObjectList(Collection<Value> collection)
    {
        super(collection);
    }

    /**
     * Removes the first element of this list
     */
    public void removeFirst()
    {
        ((LinkedList<Value>) backingList()).removeFirst();
    }
}
