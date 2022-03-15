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

import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.lexakai.DiagramIo;
import com.telenav.kivakit.core.lexakai.DiagramProgress;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An {@link OutputStream} that reports progress as bytes are written.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramProgress.class)
@UmlClassDiagram(diagram = DiagramIo.class)
@LexakaiJavadoc(complete = true)
public class ProgressiveOutputStream extends OutputStream
{
    private final OutputStream output;

    private final ProgressReporter reporter;

    public ProgressiveOutputStream(OutputStream output)
    {
        this(output, BroadcastingProgressReporter.create());
    }

    public ProgressiveOutputStream(OutputStream output, ProgressReporter reporter)
    {
        this.output = output;
        this.reporter = reporter;
    }

    @Override
    public void close() throws IOException
    {
        output.close();
    }

    @Override
    public void flush() throws IOException
    {
        output.flush();
    }

    @Override
    public void write(byte[] bytes, int offset, int length) throws IOException
    {
        output.write(bytes, offset, length);
    }

    @Override
    public void write(byte[] bytes) throws IOException
    {
        output.write(bytes);
    }

    @Override
    public void write(int _byte) throws IOException
    {
        reporter.next();
        output.write(_byte);
    }
}
