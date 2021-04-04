////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.bits.io.output;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayBitIo;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.OutputStream;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * An output stream of bits
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArrayBitIo.class)
public class BitOutput extends BaseBitWriter
{
    private final OutputStream out;

    /**
     * Construct from an output stream
     */
    public BitOutput(final OutputStream out)
    {
        this.out = out;
    }

    /**
     * Close the stream
     */
    @Override
    public void onClose()
    {
        IO.close(out);
    }

    @Override
    protected void onFlush(final byte value)
    {
        unsupported("Cannot flush bits on an output stream");
    }

    /**
     * Writes a byte to the output stream
     */
    @Override
    protected void onWrite(final byte value)
    {
        try
        {
            out.write(value);
        }
        catch (final IOException e)
        {
            throw new IllegalStateException("Unable to write " + value, e);
        }
    }
}
