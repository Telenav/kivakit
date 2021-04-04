////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.compression;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceCompression;

import java.io.OutputStream;

@UmlClassDiagram(diagram = DiagramResourceCompression.class)
public interface Compressor
{
    /**
     * @param out The output stream to compress
     * @return The compressed output stream
     */
    OutputStream compressed(OutputStream out);
}
