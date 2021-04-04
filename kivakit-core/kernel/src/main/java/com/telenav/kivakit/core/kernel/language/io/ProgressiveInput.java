package com.telenav.kivakit.core.kernel.language.io;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.progress.reporters.Progress;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIo.class)
public class ProgressiveInput extends InputStream
{
    private final InputStream input;

    private final ProgressReporter reporter;

    public ProgressiveInput(final InputStream input, final ProgressReporter reporter)
    {
        this.input = input;
        this.reporter = reporter;
    }

    public ProgressiveInput(final InputStream input)
    {
        this(input, Progress.create());
    }

    @Override
    public int available() throws IOException
    {
        return input.available();
    }

    @Override
    public void close() throws IOException
    {
        input.close();
        reporter.end();
    }

    @Override
    public int read(final byte[] bytes, final int offset, final int length) throws IOException
    {
        final var read = input.read(bytes, offset, length);
        if (read > 0)
        {
            reporter.next(read);
        }
        return read;
    }

    @Override
    public int read(final byte[] bytes) throws IOException
    {
        final var read = input.read(bytes);
        if (read > 0)
        {
            reporter.next(read);
        }
        return read;
    }

    @Override
    public int read() throws IOException
    {
        final var read = input.read();
        if (read > 0)
        {
            reporter.next();
        }
        return read;
    }

    @Override
    public synchronized void reset()
    {
        reporter.reset();
        try
        {
            super.reset();
        }
        catch (final IOException ignored)
        {
        }
    }
}
