////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.io;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIo;
import com.telenav.kivakit.core.value.count.ByteSized;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;

/**
 * An output stream that keeps track of how many bytes have been written. The number of bytes written is available
 * through {@link #sizeInBytes()}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIo.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)

public class ByteSizedOutputStream extends OutputStream implements ByteSized
{
    /** The underlying output stream */
    private final OutputStream out;

    /** The number of bytes written */
    private long size;

    /**
     * @param out The output stream to wrap
     */
    public ByteSizedOutputStream(OutputStream out)
    {
        this.out = out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException
    {
        out.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException
    {
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes sizeInBytes()
    {
        return bytes(size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte @NotNull [] b) throws IOException
    {
        out.write(b);
        size += b.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte @NotNull [] b, int off, int length) throws IOException
    {
        out.write(b, off, length);
        size += length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int b) throws IOException
    {
        out.write(b);
        size++;
    }
}
