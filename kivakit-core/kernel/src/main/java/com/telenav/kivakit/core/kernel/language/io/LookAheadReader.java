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

package com.telenav.kivakit.core.kernel.language.io;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.Reader;

/**
 * A simple stream that allows you to peek at what is coming next in an input stream.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIo.class)
public class LookAheadReader extends Reader
{
    public static final int END_OF_STREAM = -1;

    private int lineNumber;

    private final Reader in;

    private int current = END_OF_STREAM;

    private int lookAhead = END_OF_STREAM;

    public LookAheadReader(final Reader in)
    {
        // Save the underlying input stream
        this.in = in;

        // Prime the look ahead value.
        next();

        // If there is more data then prime the current value.
        if (lookAhead != END_OF_STREAM)
        {
            // read the second byte too so lookAhead() will work
            next();
        }
    }

    /**
     * Checks whether the input stream is at the end of line. Note that when the method returns true the stream should
     * be pointing to the line terminator, 0x0A.
     *
     * @return true if the input stream is at the end of a line, false otherwise.
     */
    public boolean atEndOfLine()
    {
        if (current() == 0x0A)
        {
            return true;
        }
        else if (current() == 0x0D && lookAhead() == 0x0A)
        {
            next();
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void close()
    {
        try
        {
            in.close();
        }
        catch (final IOException ignored)
        {
        }
    }

    /**
     * @return The current character in the stream or END_OF_STREAM if the stream is out of input
     */
    public final int current()
    {
        return current;
    }

    /**
     * @return True if the stream can be advanced with {@link #next()}
     */
    public final boolean hasNext()
    {
        return current != END_OF_STREAM;
    }

    public int lineNumber()
    {
        return lineNumber;
    }

    /**
     * Calling this method does not advance the stream.
     *
     * @return The next value (looking ahead) in the stream.
     */
    public int lookAhead()
    {
        return lookAhead;
    }

    /**
     * Moves the stream to the next value
     */
    public final void next()
    {
        current = lookAhead;
        if (current == '\n')
        {
            lineNumber++;
        }
        try
        {
            lookAhead = in.read();
        }
        catch (final IOException e)
        {
            throw new IllegalStateException("Cannot read from input", e);
        }
    }

    @Override
    public int read(final char[] buffer, final int offset, final int length)
    {
        var i = offset;
        var count = 0;
        while (current != END_OF_STREAM && count++ < length)
        {
            buffer[i++] = (char) current;
            next();
        }
        return count > 0 ? count : -1;
    }
}
