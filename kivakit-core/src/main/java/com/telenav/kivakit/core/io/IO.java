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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIo;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveInputStream;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.io.IO.CopyStyle.BUFFERED;

/**
 * Utility methods for buffering, closing, copying and flushing streams. All methods take a listener (except for the
 * buffer methods), catch all exceptions and transmit problems with relevant information with the listener.
 *
 * <p><b>Buffering</b></p>
 *
 * <ul>
 *     <li>{@link #buffer(InputStream)}</li>
 *     <li>{@link #buffer(OutputStream)}</li>
 * </ul>
 *
 * <p><b>Reading</b></p>
 *
 * <ul>
 *     <li>{@link #readByte(Listener, InputStream)}</li>
 *     <li>{@link #readBytes(Listener, InputStream)}</li>
 *     <li>{@link #skip(Listener, InputStream, long)}</li>
 *     <li>{@link #readString(Listener, Reader)}</li>
 *     <li>{@link #readString(Listener, InputStream)}</li>
 * </ul>
 *
 * <p><b>Copying</b></p>
 *
 * <ul>
 *     <li>{@link #copy(Listener, InputStream, OutputStream)}</li>
 *     <li>{@link #copy(Listener, InputStream, OutputStream, CopyStyle)}</li>
 *     <li>{@link #copyAndClose(Listener, InputStream, OutputStream)}</li>
 * </ul>
 *
 * <p><b>Flushing</b></p>
 *
 * <ul>
 *     <li>{@link #flush(Listener, OutputStream)}</li>
 *     <li>{@link #flush(Listener, Writer)}</li>
 * </ul>
 *
 * <p><b>Closing</b></p>
 *
 * <ul>
 *     <li>{@link #close(Listener, AutoCloseable)}</li>
 *     <li>{@link #close(Listener, InputStream)}</li>
 *     <li>{@link #close(Listener, OutputStream)}</li>
 *     <li>{@link #close(Listener, Reader)}</li>
 *     <li>{@link #close(Listener, Writer)}</li>
 *     <li>{@link #close(Listener, ZipFile)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIo.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class IO
{
    /**
     * Returns the given input stream buffered, if it is not already
     */
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

    /**
     * Returns the given output stream buffered, if it is not already
     */
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
     * Closes the given closeable
     */
    public static void close(Listener listener, AutoCloseable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (Exception e)
            {
                listener.problem(e, "Can't close AutoCloseable $", closeable);
            }
        }
    }

    /**
     * Closes the given input stream
     */
    public static void close(Listener listener, InputStream in)
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
            listener.problem(e, "Can't close input stream");
        }
    }

    /**
     * Closes the given output stream
     */
    public static void close(Listener listener, OutputStream out)
    {
        try
        {
            if (out != null)
            {
                out.close();
            }
        }
        catch (Exception e)
        {
            listener.problem(e, "Can't close input stream");
        }
    }

    /**
     * Closes the given reader
     */
    public static void close(Listener listener, Reader reader)
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
            listener.problem(e, "Can't close reader");
        }
    }

    /**
     * Closes the given writer
     */
    public static void close(Listener listener, Writer writer)
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
            listener.problem(e, "Can't close writer");
        }
    }

    /**
     * Closes the given zip file
     */
    public static void close(Listener listener, ZipFile zip)
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
            listener.problem(e, "Can't close input stream");
        }
    }

    /**
     * Copies the given input to the given output
     */
    public static boolean copy(Listener listener, InputStream input, OutputStream output)
    {
        return copy(listener, input, output, BUFFERED);
    }

    /**
     * Copy an input stream to an output stream. The streams will be automatically buffered for efficiency if they are
     * not already buffered when {@link CopyStyle} is {@link CopyStyle#BUFFERED}. Note that there is no
     * {@link ProgressReporter} argument to this method even though it could take a long time. The reason is that
     * resources are opened using a progress reporter as {@link ProgressiveInputStream} stream, which does progress
     * reporting.
     *
     * @return True if the copy succeeded.
     */
    public static boolean copy(Listener listener, InputStream input, OutputStream output, CopyStyle style)
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
            listener.problem(e, "Could not copy streams ${debug} to ${debug}", input, output);
            return false;
        }
    }

    /**
     * Copy an input stream to an output stream. The streams will be automatically buffered for efficiency if they are
     * not already buffered and when the operation completes, both streams will be closed.
     *
     * @return True if the copy succeeded.
     */
    public static boolean copyAndClose(Listener listener,
                                       InputStream input,
                                       OutputStream output)
    {
        ensureNotNull(input);
        ensureNotNull(output);

        try
        {
            return copy(listener, input, output);
        }
        finally
        {
            close(listener, input);
            close(listener, output);
        }
    }

    /**
     * Flushes the given output stream
     */
    public static boolean flush(Listener listener, OutputStream out)
    {
        try
        {
            out.flush();
            return true;
        }
        catch (IOException e)
        {
            listener.problem(e, "Couldn't flush output stream");
            return false;
        }
    }

    /**
     * Flushes the given writer
     */
    public static void flush(Listener listener, Writer out)
    {
        try
        {
            out.flush();
        }
        catch (IOException e)
        {
            listener.problem(e, "Couldn't flush writer");
        }
    }

    /**
     * Reads a single byte from the given input
     */
    public static int readByte(Listener listener, InputStream in)
    {
        try
        {
            return in.read();
        }
        catch (IOException e)
        {
            listener.problem(e, "OperationFailed reading from input stream");
            return -1;
        }
    }

    /**
     * Reads all bytes from the given input
     */
    public static byte[] readBytes(Listener listener, InputStream in)
    {
        var out = new ByteArrayOutputStream();
        if (copyAndClose(listener, in, out))
        {
            return out.toByteArray();
        }
        return null;
    }

    /**
     * Reads a string from the given input
     */
    public static String readString(Listener listener, InputStream in)
    {
        return readString(listener, new InputStreamReader(buffer(in)));
    }

    /**
     * Reads a string from the given input
     */
    public static String readString(Listener listener, Reader in)
    {
        try
        {
            return new BufferedReader(in)
                    .lines()
                    .collect(Collectors.joining("\n"))
                    .trim();
        }
        catch (Exception e)
        {
            listener.problem(e, "Unable to read string from input stream");
            return null;
        }
    }

    /**
     * Skips the given number of bytes in the given input stream
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void skip(Listener listener, InputStream in, long offset)
    {
        try
        {
            in.skip(offset);
        }
        catch (IOException e)
        {
            listener.problem(e, "Can't skip bytes on input stream");
        }
    }

    /**
     * The style to copy in, either buffered or unbuffered
     */
    public enum CopyStyle
    {
        BUFFERED,
        UNBUFFERED
    }
}
