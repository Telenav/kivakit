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

package com.telenav.kivakit.resource.compression.archive;

import com.telenav.kivakit.kernel.interfaces.code.Unchecked;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceArchive;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;

/**
 * A zip entry in a {@link ZipArchive} that is a {@link WritableResource}.
 *
 * @author jonathanl (shibo)
 * @see ZipArchive
 */
@UmlClassDiagram(diagram = DiagramResourceArchive.class)
@UmlExcludeSuperTypes({ AutoCloseable.class })
@LexakaiJavadoc(complete = true)
public class ZipEntry extends BaseWritableResource implements AutoCloseable
{
    private final Path path;

    private InputStream in;

    private OutputStream out;

    public ZipEntry(final ZipArchive archive, final Path path)
    {
        this.path = path;
    }

    @Override
    public void close()
    {
        IO.close(in);
        in = null;

        IO.close(out);
        out = null;
    }

    @Override
    public Boolean isWritable()
    {
        return false;
    }

    @KivaKitIncludeProperty
    @Override
    public Time lastModified()
    {
        return Unchecked.of(() -> Time.milliseconds(Files.getLastModifiedTime(path).toMillis())).orNull();
    }

    @Override
    public InputStream onOpenForReading()
    {
        if (in == null)
        {
            final var in = Unchecked.of(() -> Files.newInputStream(path)).orNull();
            if (in != null)
            {
                this.in = IO.buffer(in);
            }
            else
            {
                fatal("Unable to open $ for reading", this);
            }
        }
        return in;
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        if (out == null)
        {
            final var out = Unchecked.of(() -> Files.newOutputStream(path, CREATE)).orNull();
            if (out != null)
            {
                this.out = IO.buffer(out);
            }
            else
            {
                fatal("Unable to open $ for writing", this);
            }
        }
        return out;
    }

    @KivaKitIncludeProperty
    @Override
    public FilePath path()
    {
        return FilePath.filePath(path);
    }

    @KivaKitIncludeProperty
    @Override
    public Bytes sizeInBytes()
    {
        return Unchecked.of(() -> Bytes.bytes(Files.size(path))).orNull();
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
