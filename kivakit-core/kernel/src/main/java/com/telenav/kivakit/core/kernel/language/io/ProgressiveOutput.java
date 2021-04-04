package com.telenav.kivakit.core.kernel.language.io;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.progress.reporters.Progress;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIo.class)
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
