////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.io;

import com.telenav.kivakit.core.kernel.interfaces.io.ByteSized;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.OutputStream;

@UmlClassDiagram(diagram = DiagramLanguageIo.class)
public class ByteSizedOutput extends OutputStream implements ByteSized
{
    private long size;

    private final OutputStream out;

    public ByteSizedOutput(final OutputStream out)
    {
        this.out = out;
    }

    @Override
    public Bytes bytes()
    {
        return Bytes.bytes(size);
    }

    @Override
    public void close() throws IOException
    {
        out.close();
    }

    @Override
    public void flush() throws IOException
    {
        out.flush();
    }

    @Override
    public void write(final byte[] b) throws IOException
    {
        out.write(b);
        size += b.length;
    }

    @Override
    public void write(final byte[] b, final int off, final int length) throws IOException
    {
        out.write(b, off, length);
        size += length;
    }

    @Override
    public void write(final int b) throws IOException
    {
        out.write(b);
        size++;
    }
}
