////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.io;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.io.ProgressiveOutput;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceIo;

import java.io.OutputStream;

/**
 * Interface to something that can potentially be opened for writing.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceIo.class)
public interface Writable
{
    /**
     * @return True if this write-openable object can be written to
     */
    boolean isWritable();

    /**
     * Opens the output stream to this writable
     */
    OutputStream onOpenForWriting();

    /**
     * Opens something that can be written to, returning an output stream.
     *
     * @return The output stream to write to
     */
    default OutputStream openForWriting()
    {
        return onOpenForWriting();
    }

    /**
     * Opens something that can be written to, returning an output stream. The given progress reporter is called for
     * each byte that is written to the output stream. The caller may wish to initialize the progress reporter with the
     * number of bytes it plans to write via {@link ProgressReporter#steps(Count)}  to allow the reporter to report the
     * percent complete as the stream is written.
     *
     * @param reporter A progress reporter that is called for each byte that is written
     * @return The output stream to write to
     */
    default OutputStream openForWriting(final ProgressReporter reporter)
    {
        return new ProgressiveOutput(onOpenForWriting(), reporter);
    }
}
