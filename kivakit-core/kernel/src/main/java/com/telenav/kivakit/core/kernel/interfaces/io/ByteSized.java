////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.io;

import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An interface to any object whose size can be measured in bytes.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public interface ByteSized
{
    /**
     * @return The number of bytes in this object
     */
    Bytes bytes();

    default boolean isLargerThan(final ByteSized that)
    {
        return bytes().isGreaterThan(that.bytes());
    }

    default boolean isSmallerThan(final ByteSized that)
    {
        return bytes().isLessThan(that.bytes());
    }
}
