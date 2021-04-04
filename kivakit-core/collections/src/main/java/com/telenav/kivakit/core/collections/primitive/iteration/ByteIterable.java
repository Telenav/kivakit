////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.iteration;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Gets an iterator which can produce a sequence of values. A new iterator is retrieved each time the {@link
 * #iterator()} method is called.
 *
 * @author jonathanl (shibo)
 * @see ByteIterator
 */
@UmlClassDiagram(diagram = DiagramPrimitiveCollection.class)
public interface ByteIterable
{
    /**
     * @return A new iterator
     */
    ByteIterator iterator();
}
