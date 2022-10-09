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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;

/**
 * Reads the provided {@link ReadableResource} as a series of lines, reporting progress to the given
 * {@link ProgressReporter}.
 *
 * <p><b>Reading Lines</b></p>
 *
 * <ul>
 *     <li>{@link #lines()}</li>
 *     <li>{@link #lines(Consumer)}</li>
 *     <li>{@link #stream()}</li>
 * </ul>
 *
 * <p><b>NOTE</b></p>
 *
 * <p>
 * The {@link #stream()} method should be used in an idiom like this to avoid a resource leak:
 * <pre>
 * try (var stream = stream())
 * {
 *     [...]
 * }</pre>
 * </p>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class LineReader extends BaseRepeater
{
    /** The resource to read */
    private final ReadableResource resource;

    /** The handler for reporting progress */
    private final ProgressReporter reporter;

    /**
     * @param resource The resource to read
     * @param reporter The progress reporter to call after each line is read
     */
    public LineReader(@NotNull ReadableResource resource,
                      @NotNull ProgressReporter reporter)
    {
        this.resource = resource;
        this.reporter = reporter;
    }

    /**
     * @param resource The resource to read
     */
    public LineReader(@NotNull ReadableResource resource)
    {
        this(resource, nullProgressReporter());
    }

    /**
     * Returns the lines in this resource as a list of strings
     */
    public StringList lines()
    {
        var lines = stringList();
        try (var stream = stream())
        {
            stream.forEach(lines::add);
        }
        return lines;
    }

    /**
     * Calls the given consumer with each line. This method ensures that the resource stream is closed.
     */
    public void lines(@NotNull Consumer<String> consumer)
    {
        try (var reader = new LineNumberReader(listenTo(resource.reader(reporter)).textReader()))
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
        catch (Exception e)
        {
            problem(e, "Error reading lines from: $", resource);
        }
    }

    /**
     * Returns the lines produced by this reader as a {@link Stream}. If the resource cannot be read from, an empty
     * stream is returned and a problem is broadcast.
     * <p><b>NOTE</b></p>
     * The resource input stream is closed only when the stream is closed. Failing to close the stream will result in a
     * resource leak.
     */
    public Stream<String> stream()
    {
        var reader = new LineNumberReader(listenTo(resource.reader(reporter)).textReader());
        try
        {
            reporter.start();
            return reader.lines().peek(line -> reporter.next()).onClose(closer(reader));
        }
        catch (Exception e)
        {
            IO.close(this, reader);
            problem("Unable to read from: $", resource);
            return Stream.empty();
        }
        finally
        {
            reporter.end();
        }
    }

    private Runnable closer(@NotNull Closeable closeable)
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
