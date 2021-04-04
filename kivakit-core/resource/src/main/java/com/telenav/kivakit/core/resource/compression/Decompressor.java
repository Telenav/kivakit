////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.compression;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceCompression;

import java.io.InputStream;

@UmlClassDiagram(diagram = DiagramResourceCompression.class)
public interface Decompressor
{
    /**
     * @param in The input stream
     * @return The decompressed input stream
     */
    InputStream decompressed(InputStream in);
}
