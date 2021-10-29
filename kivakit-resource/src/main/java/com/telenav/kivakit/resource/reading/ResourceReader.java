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

import com.telenav.kivakit.kernel.data.conversion.Converter;
import com.telenav.kivakit.kernel.interfaces.string.StringSource;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.io.StringReader;
import com.telenav.kivakit.kernel.language.iteration.Iterables;
import com.telenav.kivakit.kernel.language.iteration.Next;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * Resource reader provides a variety of convenient ways of reading a resource, including as an array of bytes ({@link
 * #bytes()}), as a single string ({@link #string()}), as lines ({@link #linesAsStringList()}), and as objects with
 * ({@link #objectList(Converter, ProgressReporter)}, {@link #objectSet(Converter, ProgressReporter)}, and {@link
 * #objects(Converter, ProgressReporter)}). To read the resource as text, a {@link java.io.Reader} can be retrieved with
 * {@link #textReader()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlExcludeSuperTypes
@LexakaiJavadoc(complete = true)
public class ResourceReader implements StringSource
{
    private final Resource resource;

    private final Charset charset;

    private String value;

    private final ProgressReporter reporter;

    public ResourceReader(Resource resource, ProgressReporter reporter, Charset charset)
    {
        this.reporter = reporter;
        assert resource != null;
        this.resource = resource;
        this.charset = charset;
    }

    /**
     * @return The bytes in the resource being read
     */
    public byte[] bytes()
    {
        var in = open();
        try
        {
            return IO.readBytes(in);
        }
        finally
        {
            IO.close(in);
        }
    }

    public Iterable<String> lines(ProgressReporter reporter)
    {
        return new LineReader(resource, reporter);
    }

    public Iterable<String> lines()
    {
        return new LineReader(resource, ProgressReporter.NULL);
    }

    /**
     * @return The lines in the resource being read
     */
    public StringList linesAsStringList()
    {
        return linesAsStringList(ProgressReporter.NULL);
    }

    /**
     * @return The lines in the resource being read
     */
    public StringList linesAsStringList(ProgressReporter reporter)
    {
        var list = new StringList();
        list.addAll(lines(reporter));
        return list;
    }

    /**
     * @return The lines in the resource being read as a list of objects created by converting each line to an object
     * using the given converter.
     */
    public <T> List<T> objectList(Converter<String, T> converter, ProgressReporter reporter)
    {
        return (List<T>) addObjectsTo(new ArrayList<>(), converter, reporter);
    }

    /**
     * @return The lines in the resource being read as a set of objects created by converting each line to an object
     * using the given converter.
     */
    public <T> Set<T> objectSet(Converter<String, T> converter, ProgressReporter reporter)
    {
        return (Set<T>) addObjectsTo(new HashSet<>(), converter, reporter);
    }

    /**
     * @return The lines in the resource being read as a {@link Iterable} of objects created by converting each line to
     * an object using the given converter.
     */
    public <T> Iterable<T> objects(Converter<String, T> converter, ProgressReporter reporter)
    {
        return Iterables.iterable(() -> new Next<>()
        {
            private final Iterator<String> lines = lines(reporter).iterator();

            @Override
            public T onNext()
            {
                if (lines.hasNext())
                {
                    return converter.convert(lines.next());
                }
                return null;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String string()
    {
        return string(ProgressReporter.NULL);
    }

    public String string(ProgressReporter reporter)
    {
        if (value == null)
        {
            var reader = new StringReader(textReader());
            try
            {
                value = reader.readString(reporter);
            }
            finally
            {
                reader.close();
            }
        }
        return value;
    }

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

    /**
     * Adds objects in the resource being read to the given collection by reading lines and converting each line to an
     * object using the given converter
     *
     * @return The collection that was passed in
     */
    private <T> Collection<T> addObjectsTo(Collection<T> collection, Converter<String, T> converter,
                                           ProgressReporter reporter)
    {
        lines(reporter).forEach(line -> collection.add(converter.convert(line)));
        return collection;
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
