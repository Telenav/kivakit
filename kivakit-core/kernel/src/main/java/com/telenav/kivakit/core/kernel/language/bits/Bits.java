////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.bits;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageBits;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

/**
 * Utility methods for manipulating bits
 */
@UmlClassDiagram(diagram = DiagramLanguageBits.class)
public class Bits
{
    /**
     * @return The given number of one bits
     */
    public static long oneBits(final Count count)
    {
        var one = 1L;
        var value = 0L;
        for (var i = 0L; i < count.asInt(); i++)
        {
            value |= one;
            one <<= 1;
        }
        return value;
    }
}
