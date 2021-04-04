////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.io;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceIo;

import java.io.IOException;

/**
 * An interface like java.io.{@link java.io.Closeable}, except that it does not throw an {@link IOException}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceIo.class)
public interface Closeable extends AutoCloseable
{
    /**
     * Closes the object (often an I/O related object)
     */
    @Override
    @UmlExcludeMember
    void close();
}
