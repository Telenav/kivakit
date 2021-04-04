////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.store;

import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.collections.primitive.array.bits.io.input.BitInput;
import com.telenav.kivakit.core.collections.primitive.array.bits.io.output.BitOutput;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.kernel.language.time.Time;

/**
 * A binary store that can read data from a {@link Resource} and write data to a {@link WritableResource}
 *
 * @param <Type> The type to write/read to/from the {@link Resource}/{@link WritableResource}
 * @author matthieun
 */
public abstract class BinaryObjectStore<Type> extends BaseRepeater
{
    // The underlying resource for writing/reading
    private final Resource source;

    // Used to write
    private BitOutput out;

    // Used to read
    private BitInput in;

    /**
     * Construct
     *
     * @param listener The listener to the broadcasts
     * @param source The {@link Resource} that will store the binary data
     */
    protected BinaryObjectStore(final Listener listener, final Resource source)
    {
        addListener(listener);
        this.source = source;
    }

    /**
     * Write to the underlying resource
     *
     * @return The CheckType object to read from the {@link Resource}
     */
    public final Type read()
    {
        trace("Reading from resource ${debug}", source);
        final var start = Time.now();
        in = new BitInput(source.openForReading());
        readInteger(8); // discard little endian flag since we only support big-endian
        final var result = onRead();
        IO.close(in);
        trace("Finished reading from resource ${debug} in ${debug}", source, start.elapsedSince());
        return result;
    }

    /**
     * Write to the underlying resource
     *
     * @param value The CheckType to serialize
     */
    public final void write(final Type value)
    {
        if (!(source instanceof WritableResource) || !((WritableResource) source).isWritable())
        {
            throw new Problem("Cannot write to ${debug}", source).asException();
        }
        trace("Writing to resource ${debug}", source);
        final var start = Time.now();
        out = new BitOutput(((WritableResource) source).openForWriting());
        write(0, 8); // Ignore little endian flag since we only support big-endian
        onWrite(value);
        IO.close(out);
        trace("Finished writing to resource ${debug} in ${debug}", source, start.elapsedSince());
    }

    /**
     * @return The CheckType object to read from the {@link Resource}
     */
    protected abstract Type onRead();

    /**
     * @param value The CheckType to serialize
     */
    protected abstract void onWrite(Type value);

    /**
     * @return A bit read from the input stream
     */
    protected boolean readBit()
    {
        return in.readBit();
    }

    /**
     * @param bits The number of bits to read from the input stream
     * @return An integer representing the value in the bits read
     */
    protected int readInteger(final int bits)
    {
        return in.read(bits);
    }

    /**
     * @param bits The number of bits to read from the input stream
     * @return A long representing the value in the bits read
     */
    protected long readLong(final int bits)
    {
        return in.readLong(bits);
    }

    /**
     * Write a bit to the output stream
     *
     * @param bit The bit to write
     */
    protected void write(final boolean bit)
    {
        out.writeBit(bit);
    }

    /**
     * Write an integer to the output stream
     *
     * @param value The value to write
     * @param bits The number of bits to write to the output stream to represent that integer
     */
    protected void write(final int value, final int bits)
    {
        out.write(value, bits);
    }

    /**
     * Write a long to the output stream
     *
     * @param value The value to write
     * @param bits The number of bits to write to the output stream to represent that long
     */
    protected void write(final long value, final int bits)
    {
        out.write(value, bits);
    }
}
