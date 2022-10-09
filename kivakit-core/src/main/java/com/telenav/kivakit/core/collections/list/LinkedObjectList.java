package com.telenav.kivakit.core.collections.list;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Maximum;

import java.util.Collection;
import java.util.LinkedList;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;

/**
 * An {@link ObjectList} backed by a {@link LinkedList}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
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
        ((LinkedList<Value>) list()).removeFirst();
    }
}
