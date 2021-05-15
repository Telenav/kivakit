////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.io;

import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.progress.reporters.Progress;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An {@link OutputStream} that reports progress as bytes are written.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIo.class)
@LexakaiJavadoc(complete = true)
public class ProgressiveOutput extends OutputStream
{
    private final OutputStream output;

    private final ProgressReporter reporter;

    public ProgressiveOutput(final OutputStream output)
    {
        this(output, Progress.create());
    }

    public ProgressiveOutput(final OutputStream output, final ProgressReporter reporter)
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
    public void write(final byte[] bytes, final int offset, final int length) throws IOException
    {
        output.write(bytes, offset, length);
    }

    @Override
    public void write(final byte[] bytes) throws IOException
    {
        output.write(bytes);
    }

    @Override
    public void write(final int _byte) throws IOException
    {
        reporter.next();
        output.write(_byte);
    }
}