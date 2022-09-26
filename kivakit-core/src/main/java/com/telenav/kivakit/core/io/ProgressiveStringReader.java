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
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Reads string values from an {@link InputStream} (in a given encoding, if desired) or from a {@link Reader}. Since
 * this may be used for long strings like HTML pages or large text files, the method
 * {@link #readString(ProgressReporter)} takes a {@link ProgressReporter} to report progress as the string is read.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramIo.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED ,
            documentation = FULLY_DOCUMENTED)
public class ProgressiveStringReader
{
    /** The listener to call with problems */
    private final Listener listener;

    /** The underlying reader */
    private final Reader in;

    public ProgressiveStringReader(Listener listener, InputStream in)
    {
        this.listener = listener;
        this.in = new InputStreamReader(in);
    }

    public ProgressiveStringReader(Listener listener, InputStream in, Charset encoding)
    {
        this.listener = listener;
        this.in = new InputStreamReader(in, encoding);
    }

    public ProgressiveStringReader(Listener listener, InputStream in, String encoding)
    {
        this.listener = listener;
        try
        {
            this.in = new InputStreamReader(in, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("Can't create stream reader", e);
        }
    }

    public ProgressiveStringReader(Listener listener, Reader in)
    {
        this.listener = listener;
        this.in = in;
    }

    public void close()
    {
        IO.close(listener, in);
    }

    /**
     * Reads a string giving progress in bytes
     *
     * @param reporter The reporter to call for each byte read
     * @return The string read from input
     */
    public String readString(ProgressReporter reporter)
    {
        try
        {
            var buffer = new StringBuilder(2048);
            int value;
            reporter.start();
            while ((value = in.read()) != -1)
            {
                buffer.append((char) value);
                reporter.next();
            }
            reporter.end();
            return buffer.toString();
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Couldn't read string", e);
        }
    }
}
