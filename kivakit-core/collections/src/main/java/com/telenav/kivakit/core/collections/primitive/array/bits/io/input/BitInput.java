////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.bits.io.input;

import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitReader;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayBitIo;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A {@link BitReader} that gets input from an underlying {@link InputStream}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArrayBitIo.class)
public class BitInput extends BaseBitReader
{
    /** The input stream to read */
    private final InputStream in;

    /** The next byte, or a value < 0 if there is no next byte */
    private int next;

    /** The cursor in the input stream */
    private int cursor;

    /**
     * Construct from an input stream
     */
    public BitInput(final InputStream in)
    {
        this.in = in;
        next = IO.readByte(in);
        if (next >= 0)
        {
            cursor++;
        }
    }

    /**
     * Close the stream
     */
    @Override
    public void onClose()
    {
        IO.close(in);
    }

    @Override
    protected long byteCursor()
    {
        return cursor;
    }

    @Override
    protected void byteCursor(final long position)
    {
        unsupported();
    }

    @Override
    protected boolean hasNextByte()
    {
        return next >= 0;
    }

    @Override
    protected byte nextByte()
    {
        final var current = next;
        next = IO.readByte(in);
        cursor++;
        return (byte) current;
    }
}
