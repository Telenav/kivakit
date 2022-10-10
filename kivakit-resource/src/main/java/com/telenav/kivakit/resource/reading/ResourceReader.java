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
import com.telenav.kivakit.conversion.Converter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.io.ProgressiveStringReader;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.interfaces.string.AsString;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Resource reader provides a variety of convenient ways of reading a resource, including as an array of bytes
 * ({@link #readBytes()}), as a single string ({@link #asString()}), as lines ({@link #readLines()}), and as objects
 * with ({@link #readObjectList(Converter, ProgressReporter)}, {@link #readObjectSet(Converter, ProgressReporter)}, and
 * {@link #readObjects(Converter, Consumer, ProgressReporter)}). To read the resource as text, a {@link java.io.Reader}
 * can be retrieved with {@link #textReader()}.
 *
 * <p><b>Reading</b></p>
 *
 * <ul>
 *     <li>{@link #asString()}</li>
 *     <li>{@link #readBytes()}</li>
 *     <li>{@link #readLines()}</li>
 *     <li>{@link #readLines(Consumer, ProgressReporter)}</li>
 *     <li>{@link #readLines(ProgressReporter)}</li>
 *     <li>{@link #readObjects(Converter, Consumer, ProgressReporter)}</li>
 *     <li>{@link #readObjectList(Converter, ProgressReporter)}</li>
 *     <li>{@link #readObjectSet(Converter, ProgressReporter)}</li>
 *     <li>{@link #readText(ProgressReporter)}</li>
 *     <li>{@link #textReader()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlExcludeSuperTypes
@CodeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = UNTESTED)
public class ResourceReader extends BaseRepeater implements AsString
{
    /** The character set to read text in */
    private final Charset charset;

    /** The progress reporter to call as text is read */
    private final ProgressReporter reporter;

    /** The resource to read */
    private final Resource resource;

    /** The text */
    private String text;

    /**
     * Creates a resource reader
     *
     * @param resource The resource to read
     * @param reporter The progress reporter to call as the resource is read
     * @param charset The charset for the text content
     */
    public ResourceReader(@NotNull Resource resource,
                          @NotNull ProgressReporter reporter,
                          @NotNull Charset charset)
    {
        this.reporter = ensureNotNull(reporter);
        this.resource = ensureNotNull(resource);
        this.charset = ensureNotNull(charset);
    }

    /**
     * Creates a resource reader that reads UTF-8 encoded text
     *
     * @param resource The resource to read
     * @param reporter The progress reporter to call as the resource is read
     */
    public ResourceReader(@NotNull Resource resource,
                          @NotNull ProgressReporter reporter)
    {
        this(resource, reporter, UTF_8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString()
    {
        return readText(nullProgressReporter());
    }

    /**
     * Returns the bytes in the resource being read
     *
     * @return The bytes in this resource or null if the bytes could not be read
     */
    public byte[] readBytes()
    {
        var in = open();
        if (in != null)
        {
            return IO.readBytes(this, in);
        }
        else
        {
            problem("Unable to open: $", resource);
            return null;
        }
    }

    /**
     * Returns the lines in this text file
     *
     * @param reporter The progress reporter to call as lines are read
     */
    public StringList readLines(@NotNull ProgressReporter reporter)
    {
        return listenTo(new LineReader(resource, reporter)).lines();
    }

    /**
     * Returns the lines in this text file
     */
    public StringList readLines()
    {
        return readLines(nullProgressReporter());
    }

    /**
     * Passes the lines in this text file to the given consumer, one at a time
     *
     * @param consumer The consumer to call with each line
     * @param reporter The progress reporter to call as lines are read
     */
    public void readLines(@NotNull Consumer<String> consumer,
                          @NotNull ProgressReporter reporter)
    {
        listenTo(new LineReader(resource, reporter)).lines(consumer);
    }

    /**
     * Returns the lines in the resource being read as a list of objects created by converting each line to an object
     * using the given converter.
     */
    public <T> ObjectList<T> readObjectList(@NotNull Converter<String, T> converter,
                                            @NotNull ProgressReporter reporter)
    {
        var objects = new ObjectList<T>();
        readLines(reporter).forEach(line -> objects.add(converter.convert(line)));
        return objects;
    }

    /**
     * Returns the lines in the resource being read as a set of objects created by converting each line to an object
     * using the given converter.
     */
    public <T> ObjectSet<T> readObjectSet(@NotNull Converter<String, T> converter,
                                          @NotNull ProgressReporter reporter)
    {
        var objects = new ObjectSet<T>();
        readLines(reporter).forEach(line -> objects.add(converter.convert(line)));
        return objects;
    }

    /**
     * Returns the lines in the resource being read as a {@link Iterable} of objects created by converting each line to
     * an object using the given converter.
     */
    public <T> void readObjects(@NotNull Converter<String, T> converter,
                                @NotNull Consumer<T> consumer,
                                @NotNull ProgressReporter reporter)
    {
        listenTo(new LineReader(resource, reporter)).lines(line -> consumer.accept(converter.convert(line)));
    }

    /**
     * Returns the text in this resource
     *
     * @param reporter The reporter to call as data is read
     * @return The string
     */
    public String readText(@NotNull ProgressReporter reporter)
    {
        if (text == null)
        {
            var reader = new ProgressiveStringReader(this, textReader());
            try
            {
                text = reader.readString(reporter);
            }
            finally
            {
                reader.close();
            }
        }
        return text;
    }

    /**
     * Returns an open text {@link Reader} for this resource
     */
    public Reader textReader()
    {
        var in = open();
        if (in != null)
        {
            if (charset == null)
            {
                return new InputStreamReader(in);
            }
            else
            {
                return new InputStreamReader(in, charset);
            }
        }
        else
        {
            return fail("Couldn't read resource '$'", resource.path());
        }
    }

    @Override
    public String toString()
    {
        return resource.toString();
    }

    private InputStream open()
    {
        var size = resource.sizeInBytes();
        if (size != null)
        {
            reporter.steps(size);
        }
        return resource.openForReading(reporter);
    }
}
