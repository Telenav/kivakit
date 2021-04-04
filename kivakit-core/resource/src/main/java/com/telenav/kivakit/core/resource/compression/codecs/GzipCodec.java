////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.compression.codecs;

import com.telenav.kivakit.core.resource.compression.Codec;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceCompression;

import java.io.*;
import java.util.zip.*;

@UmlClassDiagram(diagram = DiagramResourceCompression.class)
public class GzipCodec implements Codec
{
    @Override
    public OutputStream compressed(final OutputStream out)
    {
        try
        {
            return new GZIPOutputStream(out);
        }
        catch (final IOException e)
        {
            throw new Problem(e, "Cannot open GZIP output stream").asException();
        }
    }

    @Override
    public InputStream decompressed(final InputStream in)
    {
        try
        {
            return new GZIPInputStream(in);
        }
        catch (final IOException e)
        {
            throw new Problem(e, "Cannot open GZIP input stream").asException();
        }
    }
}
