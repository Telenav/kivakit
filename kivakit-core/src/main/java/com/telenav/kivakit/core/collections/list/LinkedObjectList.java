package com.telenav.kivakit.core.collections.list;

import com.telenav.kivakit.core.value.count.Maximum;

import java.util.Collection;
import java.util.LinkedList;

public class LinkedObjectList<Element> extends ObjectList<Element>
{
    public LinkedObjectList()
    {
        super(new LinkedList<>());
    }

    public LinkedObjectList(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public LinkedObjectList(final Collection<Element> collection)
    {
        super(collection);
    }
}
