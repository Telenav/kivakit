////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.io;

import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.io.*;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

import static com.telenav.kivakit.core.kernel.language.io.IO.CopyStyle.BUFFERED;

/**
 * Utility methods for buffering, closing, copying and flushing streams.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIo.class)
public class IO
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static BufferedInputStream buffer(final InputStream in)
    {
        if (in instanceof BufferedInputStream)
        {
            return (BufferedInputStream) in;
        }
        return new BufferedInputStream(in);
    }

    public static BufferedOutputStream buffer(final OutputStream out)
    {
        if (out instanceof BufferedOutputStream)
        {
            return (BufferedOutputStream) out;
        }
        return new BufferedOutputStream(out);
    }

    /**
     * Added writer safe close capability
     */
    public static void close(final AutoCloseable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (final Exception e)
            {
                LOGGER.problem(e, "Can't close AutoCloseable $", closeable);
            }
        }
    }

    public static void close(final InputStream in)
    {
        try
        {
            if (in != null)
            {
                in.close();
            }
        }
        catch (final IOException e)
        {
            LOGGER.problem(e, "Can't close input stream");
        }
    }

    public static void close(final OutputStream out)
    {
        try
        {
            if (out != null)
            {
                out.close();
            }
        }
        catch (final Exception ignored)
        {
        }
    }

    public static void close(final Reader reader)
    {
        try
        {
            if (reader != null)
            {
                reader.close();
            }
        }
        catch (final IOException e)
        {
            LOGGER.problem(e, "Can't close reader");
        }
    }

    /**
     * Added writer safe close capability
     */
    public static void close(final Writer writer)
    {
        try
        {
            if (writer != null)
            {
                writer.close();
            }
        }
        catch (final IOException e)
        {
            LOGGER.problem(e, "Can't close writer");
        }
    }

    public static void close(final ZipFile zip)
    {
        try
        {
            if (zip != null)
            {
                zip.close();
            }
        }
        catch (final IOException e)
        {
            LOGGER.problem(e, "Can't close input stream");
        }
    }

    public static boolean copy(final InputStream input, final OutputStream output)
    {
        return copy(input, output, BUFFERED);
    }

    /**
     * Copy an input stream to an output stream. The streams will be automatically buffered for efficiency if they are
     * not already buffered when {@link CopyStyle} is {@link CopyStyle#BUFFERED}. Note that there is no {@link
     * ProgressReporter} argument to this method even though it could take a long time. The reason is that resources are
     * opened using a progress reporter as {@link ProgressiveInput} stream, which does progress reporting.
     *
     * @return True if the copy succeeded.
     */
    public static boolean copy(final InputStream input, final OutputStream output, final CopyStyle style)
    {
        final var in = style == BUFFERED ? buffer(input) : input;
        final var out = style == BUFFERED ? buffer(output) : output;
        try
        {
            final var buffer = new byte[style == BUFFERED ? 4096 : 1];
            int bytes;
            while ((bytes = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, bytes);
            }
            out.flush();
            return true;
        }
        catch (final IOException e)
        {
            LOGGER.problem(e, "Could not copy streams ${debug} to ${debug}", input, output);
            return false;
        }
    }

    /**
     * Copy an input stream to an output stream. The streams will be automatically buffered for efficiency if they are
     * not already buffered and when the operation completes, both streams will be closed.
     *
     * @return True if the copy succeeded.
     */
    public static boolean copyAndClose(final InputStream input,
                                       final OutputStream output)
    {
        try
        {
            return copy(input, output);
        }
        finally
        {
            close(input);
            close(output);
        }
    }

    public static boolean flush(final OutputStream out)
    {
        try
        {
            out.flush();
            return true;
        }
        catch (final IOException e)
        {
            LOGGER.problem(e, "Couldn't flush output stream");
            return false;
        }
    }

    public static void flush(final Writer out)
    {
        try
        {
            out.flush();
        }
        catch (final IOException ignored)
        {
        }
    }

    /**
     * Convenience method to print a formatted message to the console
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    @UmlExcludeMember
    public static void println(final String message, final Object... arguments)
    {
        System.out.println(Message.format(message, arguments));
    }

    public static int readByte(final InputStream in)
    {
        try
        {
            return in.read();
        }
        catch (final IOException e)
        {
            LOGGER.problem(e, "OperationFailed reading from input stream");
            return -1;
        }
    }

    public static byte[] readBytes(final InputStream in)
    {
        final var out = new ByteArrayOutputStream();
        if (copyAndClose(in, out))
        {
            return out.toByteArray();
        }
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void skip(final InputStream in, final long offset)
    {
        try
        {
            in.skip(offset);
        }
        catch (final IOException e)
        {
            LOGGER.problem(e, "Can't skip bytes on input stream");
        }
    }

    public static String string(final InputStream in)
    {
        return new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n")).trim();
    }

    public enum CopyStyle
    {
        BUFFERED,
        UNBUFFERED
    }
}
