package com.telenav.kivakit.core.collections.list;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.value.count.Maximum;

import java.util.Collection;
import java.util.LinkedList;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.MORE_TESTING_NEEDED;

/**
 * An {@link ObjectList} backed by a {@link LinkedList}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            testing = MORE_TESTING_NEEDED,
            documentation = FULLY_DOCUMENTED)
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
