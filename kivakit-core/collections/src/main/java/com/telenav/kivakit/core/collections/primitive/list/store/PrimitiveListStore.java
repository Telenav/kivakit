////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.list.store;

import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveList.class)
public abstract class PrimitiveListStore extends PrimitiveCollection
{
    public PrimitiveListStore(final String objectName)
    {
        super(objectName);
    }

    protected PrimitiveListStore()
    {
    }
}
