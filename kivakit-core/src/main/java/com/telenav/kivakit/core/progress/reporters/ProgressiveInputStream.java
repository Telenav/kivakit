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

package com.telenav.kivakit.core.progress.reporters;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIo;
import com.telenav.kivakit.core.internal.lexakai.DiagramProgress;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * An {@link InputStream} that reports progress as bytes are read.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIo.class)
@UmlClassDiagram(diagram = DiagramProgress.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ProgressiveInputStream extends InputStream
{
    /** The underlying input stream */
    private final InputStream input;

    /** The progress reporter to notify as the stream is read */
    private final ProgressReporter reporter;

    /**
     * Creates a progressive input stream that reports bytes read from the given input stream to the given reporter
     *
     * @param input The input stream
     * @param reporter The reporter
     */
    public ProgressiveInputStream(InputStream input, ProgressReporter reporter)
    {
        this.input = input;
        this.reporter = reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() throws IOException
    {
        return input.available();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException
    {
        input.close();
        reporter.end();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte @NotNull [] bytes, int offset, int length) throws IOException
    {
        var read = input.read(bytes, offset, length);
        if (read > 0)
        {
            reporter.next(read);
        }
        return read;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte @NotNull [] bytes) throws IOException
    {
        var read = input.read(bytes);
        if (read > 0)
        {
            reporter.next(read);
        }
        return read;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException
    {
        var read = input.read();
        if (read > 0)
        {
            reporter.next();
        }
        return read;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void reset()
    {
        reporter.reset();
        try
        {
            super.reset();
        }
        catch (IOException ignored)
        {
        }
    }

    @Override
    public String toString()
    {
        return "[ProgressiveInputStream input = " + input.getClass().getSimpleName() + "]";
    }
}
