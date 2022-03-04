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

import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveInputStream;
import com.telenav.kivakit.core.project.lexakai.DiagramIo;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

import static com.telenav.kivakit.core.io.IO.CopyStyle.BUFFERED;

/**
 * Utility methods for buffering, closing, copying and flushing streams.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIo.class)
@LexakaiJavadoc(complete = true)
public class IO
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static BufferedInputStream buffer(InputStream in)
    {
        if (in instanceof BufferedInputStream)
        {
            return (BufferedInputStream) in;
        }
        if (in != null)
        {
            return new BufferedInputStream(in);
        }
        return null;
    }

    public static BufferedOutputStream buffer(OutputStream out)
    {
        if (out instanceof BufferedOutputStream)
        {
            return (BufferedOutputStream) out;
        }
        if (out != null)
        {
            return new BufferedOutputStream(out);
        }
        return null;
    }

    /**
     * Added writer safe close capability
     */
    public static void close(AutoCloseable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (Exception e)
            {
                LOGGER.problem(e, "Can't close AutoCloseable $", closeable);
            }
        }
    }

    public static void close(InputStream in)
    {
        try
        {
            if (in != null)
            {
                in.close();
            }
        }
        catch (IOException e)
        {
            LOGGER.problem(e, "Can't close input stream");
        }
    }

    public static void close(OutputStream out)
    {
        try
        {
            if (out != null)
            {
                out.close();
            }
        }
        catch (Exception ignored)
        {
        }
    }

    public static void close(Reader reader)
    {
        try
        {
            if (reader != null)
            {
                reader.close();
            }
        }
        catch (IOException e)
        {
            LOGGER.problem(e, "Can't close reader");
        }
    }

    /**
     * Added writer safe close capability
     */
    public static void close(Writer writer)
    {
        try
        {
            if (writer != null)
            {
                writer.close();
            }
        }
        catch (IOException e)
        {
            LOGGER.problem(e, "Can't close writer");
        }
    }

    public static void close(ZipFile zip)
    {
        try
        {
            if (zip != null)
            {
                zip.close();
            }
        }
        catch (IOException e)
        {
            LOGGER.problem(e, "Can't close input stream");
        }
    }

    public static boolean copy(InputStream input, OutputStream output)
    {
        return copy(input, output, BUFFERED);
    }

    /**
     * Copy an input stream to an output stream. The streams will be automatically buffered for efficiency if they are
     * not already buffered when {@link CopyStyle} is {@link CopyStyle#BUFFERED}. Note that there is no {@link
     * ProgressReporter} argument to this method even though it could take a long time. The reason is that resources are
     * opened using a progress reporter as {@link ProgressiveInputStream} stream, which does progress reporting.
     *
     * @return True if the copy succeeded.
     */
    public static boolean copy(InputStream input, OutputStream output, CopyStyle style)
    {
        var in = style == BUFFERED ? buffer(input) : input;
        var out = style == BUFFERED ? buffer(output) : output;
        try
        {
            var buffer = new byte[style == BUFFERED ? 4096 : 1];
            int bytes;
            while ((bytes = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, bytes);
            }
            out.flush();
            return true;
        }
        catch (IOException e)
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
    public static boolean copyAndClose(InputStream input,
                                       OutputStream output)
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

    public static boolean flush(OutputStream out)
    {
        try
        {
            out.flush();
            return true;
        }
        catch (IOException e)
        {
            LOGGER.problem(e, "Couldn't flush output stream");
            return false;
        }
    }

    public static void flush(Writer out)
    {
        try
        {
            out.flush();
        }
        catch (IOException ignored)
        {
        }
    }

    /**
     * Convenience method to print a formatted message to the console
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    @UmlExcludeMember
    public static void println(String message, Object... arguments)
    {
        System.out.println(Formatter.format(message, arguments));
    }

    public static int readByte(InputStream in)
    {
        try
        {
            return in.read();
        }
        catch (IOException e)
        {
            LOGGER.problem(e, "OperationFailed reading from input stream");
            return -1;
        }
    }

    public static byte[] readBytes(InputStream in)
    {
        var out = new ByteArrayOutputStream();
        if (copyAndClose(in, out))
        {
            return out.toByteArray();
        }
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void skip(InputStream in, long offset)
    {
        try
        {
            in.skip(offset);
        }
        catch (IOException e)
        {
            LOGGER.problem(e, "Can't skip bytes on input stream");
        }
    }

    public static String string(InputStream in)
    {
        return string(new InputStreamReader(buffer(in)));
    }

    public static String string(Reader in)
    {
        return new BufferedReader(in)
                .lines()
                .collect(Collectors.joining("\n"))
                .trim();
    }

    /**
     * The style to copy in, either buffered or unbuffered
     */
    @LexakaiJavadoc(complete = true)
    public enum CopyStyle
    {
        BUFFERED,
        UNBUFFERED
    }
}
