////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.io;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;

@UmlClassDiagram(diagram = DiagramLanguageIo.class)
public class LimitedInput extends InputStream
{
    private InputStream in;

    private final Bytes limit;

    private long read;

    public LimitedInput(final InputStream in, final Bytes limit)
    {
        this.in = in;
        this.limit = limit;
    }

    @Override
    public void close()
    {
        IO.close(in);
        in = null;
    }

    @Override
    public int read() throws IOException
    {
        if (read++ > limit.asBytes())
        {
            return -1;
        }
        return in.read();
    }
}
