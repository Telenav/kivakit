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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.FULLY_TESTED;
import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An input stream built around a StringBuilder to read it.
 *
 * @author matthieun
 */
@SuppressWarnings({ "UnusedReturnValue", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramIo.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = FULLY_TESTED,
            documentation = FULLY_DOCUMENTED)
public class StringInputStream extends InputStream
{
    /**
     * How the stream should behave if characters in the string are not encodable in the target character set.
     */
    public enum EncodingErrorBehavior
    {
        /**
         * Omit unencodable characters.
         */
        OMIT,
        /**
         * Replace unencodable characters with an encodable substitute.
         */
        REPLACE,
        /**
         * Throw an exception on encoding failure (the exception will be that thrown by
         * {@link CoderResult#throwException() }).
         */
        THROW;
    }

    /** Character encoder */
    private final CharsetEncoder encoder;

    /** Buffer of characters */
    private final CharBuffer chars;

    /** Buffer where we cache encoded bytes to emit when a read call occurs */
    private final ByteBuffer outBuffer;

    /** How to fail on un-encodable characters and similar */
    private final EncodingErrorBehavior errorBehavior;

    /** True when closed */
    private boolean closed;

    /**
     * Create a new UTF-8 StringInputStream.
     *
     * @param toRead A string
     */
    public StringInputStream(CharSequence toRead)
    {
        this(toRead, null);
    }

    /**
     * Create a new input stream with the specified encoding.
     *
     * @param toRead A string
     * @param encoding An encoding
     */
    public StringInputStream(CharSequence toRead, Charset encoding)
    {
        this(toRead, encoding, -1);
    }

    /**
     * Create a new input stream with the specified encoding, with a specific buffer size for how many bytes from the
     * string should be buffered during the writing process.
     *
     * @param toRead A string
     * @param encoding An encoding
     * @param outBufferSize A buffer size - if &lt;= 0, the buffer size will based on the character set's average bytes
     * per character to accommodate the entire string
     */
    public StringInputStream(CharSequence toRead, Charset encoding, int outBufferSize)
    {
        this(toRead, encoding, outBufferSize,
                EncodingErrorBehavior.REPLACE);
    }

    /**
     * Create a new input stream with the specified encoding, with a specific buffer size for how many bytes from the
     * string should be buffered during the writing process.
     *
     * @param toRead A string
     * @param encoding An encoding
     * @param outBufferSize A buffer size - if &lt;= 0, the buffer size will based on the character set's average bytes
     * per character to accommodate the entire string
     * @param errorBehavior What to do if unmappable characters are encountered - omit them, use a substitute character,
     * or throw an IllegalStateException to wrap the original CoderResult's exception
     */
    public StringInputStream(CharSequence toRead, Charset encoding, int outBufferSize,
                             EncodingErrorBehavior errorBehavior)
    {
        // This should not involve a copy, since the returned buffer is read-only
        chars = CharBuffer.wrap(toRead);
        this.errorBehavior = errorBehavior;
        // Create an encoder
        encoder = (encoding == null
                ? UTF_8
                : encoding).newEncoder();
        if (outBufferSize <= 0)
        {
            // Come up with a buffer size that should hold the entire
            // encoded string
            int estimatedBytes = (int) max(2,
                    ceil(encoder.averageBytesPerChar() * toRead.length()));
            outBuffer = ByteBuffer.allocate(estimatedBytes);
        }
        else
        {
            outBuffer = ByteBuffer.allocate(max(2, outBufferSize));
        }
        // The buffer needs to initially have no bytes available, so the first
        // read will trigger a call to encodeSomeBytes().
        outBuffer.limit(0);
    }

    @Override
    public synchronized int available() throws IOException
    {
        // available() is documented to return an estimate(), so... estimate.
        int charsRemaining = chars.remaining();
        return (int) ceil(encoder.averageBytesPerChar() * charsRemaining);
    }

    @Override
    public synchronized void close() throws IOException
    {
        // Move to the end of the character buffer and
        // position the output buffer so it shows no remaining characters
        chars.position(chars.capacity());
        outBuffer.clear();
        outBuffer.limit(0);
        closed = true;
    }

    @Override
    public synchronized int read(byte @NotNull [] b, int off, int len) throws IOException
    {
        if (len <= 0)
        {
            return 0;
        }
        if (off + len > b.length)
        {
            throw new IllegalArgumentException("Offset + length = "
                    + (off + len) + " but array size is " + b.length);
        }
        if (!ensureBytesAvailable())
        {
            return -1;
        }
        int numBytes = min(len, outBuffer.remaining());
        outBuffer.get(b, off, numBytes);
        return numBytes;
    }

    @Override
    public synchronized int read() throws IOException
    {
        if (!ensureBytesAvailable())
        {
            return -1;
        }
        return outBuffer.get();
    }

    @Override
    public synchronized int read(byte[] b) throws IOException
    {
        if (b.length == 0)
        {
            return 0;
        }
        if (!ensureBytesAvailable())
        {
            return -1;
        }
        int numBytes = min(b.length, outBuffer.remaining());
        outBuffer.get(b, 0, numBytes);
        return numBytes;
    }

    /**
     * Rewind this stream to its start.
     *
     * @return this
     */
    public synchronized StringInputStream rewind()
    {
        // reset() would be a better name for this method, but that already
        // has a different meaning related to the mark for input streams, and
        // we do not support it.
        encoder.reset();
        outBuffer.clear();
        outBuffer.limit(0);
        chars.position(0);
        closed = false;
        return this;
    }

    @Override
    public String toString()
    {
        return "StringInputStream(chars @" + chars.position() + "/" + chars
                .capacity() + " bytes@ "
                + outBuffer.position() + "/" + outBuffer.capacity()
                + " for\n" + chars + ")";
    }

    @Override
    public synchronized long transferTo(OutputStream out) throws IOException
    {
        long result = 0;
        while (ensureBytesAvailable())
        {
            byte[] bytes = new byte[outBuffer.remaining()];
            outBuffer.get(bytes);
            out.write(bytes);
            result += bytes.length;
        }
        return result;
    }

    private boolean encodeSomeBytes() throws IOException
    {
        // This is where we do the heavy lifting - slurp up some bytes into
        // outBuffer via the encoder.  When those have been exhausted, we will
        // be called to get some more.
        //
        // We MUST be under a lock here
        assert Thread.holdsLock(this) : "Not under lock";
        assert !outBuffer.hasRemaining() : "Should not get here with unread bytes";
        if (!chars.hasRemaining())
        {
            return false;
        }
        // Zero out the position of the bytes cache and reset its limit
        outBuffer.clear();
        // Encode some characters
        CoderResult coderResult = encoder.encode(chars, outBuffer, false);
        // If something went wrong, do what the behavior we were passed suggests:
        if (coderResult.isUnmappable() || coderResult.isMalformed())
        {
            switch (errorBehavior)
            {
                case OMIT:
                    // By default, nothing gets written on an encoding error,
                    // and encoding stops when one is encountered
                    encoder.reset();
                    // The character buffer will not have had its cursor incremented,
                    // so move it past the bad characters or the next call will attemp
                    // to read the same thing and fail the same way forever.
                    chars.position(min(chars.limit(),
                            chars.position() + coderResult.length()));
                    // Prepare the buffer for reading, so any bytes preceding
                    // the error are emitted
                    outBuffer.flip();
                    // IF we're out of characters and bytes, we're done
                    return outBuffer.hasRemaining() || chars.hasRemaining();
                case THROW:
                {
                    coderResult.throwException();
                }
                case REPLACE:
                    int estimatedMangledCharacters = (int) min(1,
                            coderResult.length() / encoder.averageBytesPerChar());
                    // By default, we would have no characters to return for unencodable
                    // characters.  Imitate the behavior of the rest of the universe by
                    // filling with the unicode "replacement character" character
                    char[] c = new char[estimatedMangledCharacters];
                    // If we are in US_ASCII, we cannot use \ufffd, the standard unicode
                    // replacement character, so use *something*
                    char fillChar = encoder.canEncode('\ufffd')
                            ? '\ufffd'
                            : '-';
                    Arrays.fill(c, fillChar);
                    // Append the replacements to the output buffer
                    CharBuffer cb = CharBuffer.wrap(c);
                    encoder.encode(cb, outBuffer, false);
                    // Get the buffer past the bad characters
                    chars.position(min(chars.limit(),
                            chars.position() + coderResult
                                    .length()));
            }
        }
        // Pending: We could have the option of accepting a listener and/or throwing
        // on encoding/decoding errors
        outBuffer.flip();
        return outBuffer.hasRemaining();
    }

    private boolean ensureBytesAvailable() throws IOException
    {
        // We MUST be under a lock here
        assert Thread.holdsLock(this) : "Not under lock";
        if (closed)
        {
            // If the stream has been closed, it is likely programmer error
            // to call any read method, so better to throw than produce
            // mysterious behavior
            throw new IOException("Stream is closed.");
        }
        if (!outBuffer.hasRemaining())
        {
            // We may just be at the end of the last-read buffer - rewind
            // it and load up another encoded buffer of bytes if possible
            return encodeSomeBytes();
        }
        return true;
    }
}
