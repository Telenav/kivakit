////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map.multi;

import com.telenav.kivakit.core.collections.primitive.array.scalars.LongArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMultiMap;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Sized;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

@UmlClassDiagram(diagram = DiagramPrimitiveMultiMap.class)
public interface LongMultiMap extends Sized
{
    LongArray get(long key);

    void putAll(long key, LongArray values);

    void putAll(long key, List<? extends Quantizable> values);
}
