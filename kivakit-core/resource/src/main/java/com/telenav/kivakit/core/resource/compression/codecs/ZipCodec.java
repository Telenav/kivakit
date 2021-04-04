////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.compression.codecs;

import com.telenav.kivakit.core.resource.compression.Codec;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceCompression;

import java.io.*;
import java.util.zip.*;

@UmlClassDiagram(diagram = DiagramResourceCompression.class)
public class ZipCodec implements Codec
{
    @Override
    public OutputStream compressed(final OutputStream out)
    {
        return new ZipOutputStream(out);
    }

    @Override
    public InputStream decompressed(final InputStream in)
    {
        return new ZipInputStream(in);
    }
}
