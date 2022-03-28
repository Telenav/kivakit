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

package com.telenav.kivakit.resource.reading;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Reads the provided {@link ReadableResource} as a series of lines, reporting progress to the given {@link
 * ProgressReporter}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class LineReader extends Multicaster implements Iterable<String>
{
    /** The resource to read */
    private final ReadableResource resource;

    /** The handler for reporting progress */
    private final ProgressReporter reporter;

    public LineReader(ReadableResource resource, ProgressReporter reporter)
    {
        this.resource = resource;
        this.reporter = reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull
    Iterator<String> iterator()
    {
        return stream().iterator();
    }

    /**
     * @return The lines in this resource as a list of strings
     */
    public StringList lines()
    {
        var list = new StringList();
        list.addAll(this);
        return list;
    }

    /**
     * Calls the given consumer with each line
     */
    public void lines(Consumer<String> consumer)
    {
        var reader = new LineNumberReader(resource.reader(reporter).textReader());
        try
        {
            reporter.start();
            while (true)
            {
                var next = reader.readLine();
                if (next == null)
                {
                    reporter.end();
                    return;
                }
                reporter.next();
                consumer.accept(next);
            }
        }
        catch (IOException e)
        {
            throw new IllegalStateException(
                    "Exception thrown while reading " + resource + " at line " + reader.getLineNumber(), e);
        }
        finally
        {
            IO.close(reader);
        }
    }

    /**
     * @return The lines produced by this reader as a {@link Stream}
     */
    public Stream<String> stream()
    {
        var reader = new LineNumberReader(resource.reader(reporter).textReader());
        try
        {
            reporter.start();
            return reader.lines().peek(line -> reporter.next()).onClose(closer(reader));
        }
        catch (Exception e)
        {
            try
            {
                reader.close();
            }
            catch (IOException ex)
            {
                try
                {
                    e.addSuppressed(ex);
                }
                catch (Throwable ignore)
                {
                }
            }
            throw e;
        }
        finally
        {
            reporter.end();
        }
    }

    private Runnable closer(Closeable closeable)
    {
        return () ->
        {
            try
            {
                closeable.close();
            }
            catch (IOException e)
            {
                throw new UncheckedIOException(e);
            }
        };
    }
}
