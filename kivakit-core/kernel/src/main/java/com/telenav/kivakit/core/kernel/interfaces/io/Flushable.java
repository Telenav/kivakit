////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.io;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceIo;

/**
 * An object that can be flushed, like a queue or output stream.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceIo.class)
public interface Flushable
{
    /**
     * Flushes the object waiting not more than the give duration for this to occur.
     *
     * @param maximumWaitTime The amount of time to wait before giving up on flushing. To achieve a blocking flush,
     * simply pass in {@link Duration#MAXIMUM} as the wait time, or call {@link #flush()}.
     */
    void flush(Duration maximumWaitTime);

    /**
     * Flush the object, waiting until finished doing so.
     */
    default void flush()
    {
        flush(Duration.MAXIMUM);
    }
}
