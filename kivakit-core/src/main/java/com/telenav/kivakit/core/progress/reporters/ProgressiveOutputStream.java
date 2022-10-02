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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIo;
import com.telenav.kivakit.core.internal.lexakai.DiagramProgress;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * An {@link OutputStream} that reports progress as bytes are written.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramProgress.class)
@UmlClassDiagram(diagram = DiagramIo.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class ProgressiveOutputStream extends OutputStream
{
    /** The underlying output stream */
    private final OutputStream output;

    /** The progress reporter to notify as the stream is written */
    private final ProgressReporter reporter;

    /**
     * Creates a progressive input stream that reports bytes written to the given output stream to the given reporter
     *
     * @param output The output stream
     * @param reporter The reporter
     */
    public ProgressiveOutputStream(OutputStream output, ProgressReporter reporter)
    {
        this.output = output;
        this.reporter = reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException
    {
        output.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException
    {
        output.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte @NotNull [] bytes, int offset, int length) throws IOException
    {
        output.write(bytes, offset, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte @NotNull [] bytes) throws IOException
    {
        output.write(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int _byte) throws IOException
    {
        reporter.next();
        output.write(_byte);
    }
}
