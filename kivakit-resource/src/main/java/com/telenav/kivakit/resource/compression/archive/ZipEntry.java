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

import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.language.object.ObjectFormatter;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.resource.lexakai.DiagramResourceArchive;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

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
    private InputStream in;

    private OutputStream out;

    private final Path path;

    public ZipEntry(FileSystem filesystem, Path path)
    {
        this.path = filesystem.getPath(path.toString());
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
    public Time createdAt()
    {
        return UncheckedCode.of(() -> Time.epochMilliseconds(Files.readAttributes(path, BasicFileAttributes.class)
                .creationTime().toMillis())).orNull();
    }

    @Override
    public Boolean isWritable()
    {
        return false;
    }

    @KivaKitIncludeProperty
    @Override
    public Time modifiedAt()
    {
        return UncheckedCode.of(() -> Time.epochMilliseconds(Files.getLastModifiedTime(path).toMillis())).orNull();
    }

    @Override
    public InputStream onOpenForReading()
    {
        if (in == null)
        {
            var in = UncheckedCode.of(() -> Files.newInputStream(path)).orNull();
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
            OutputStream out;
            try
            {
                var parent = path.getParent();
                if (parent != null)
                {
                    Files.createDirectories(parent);
                }
                out = Files.newOutputStream(path, CREATE, WRITE);
                if (out != null)
                {
                    this.out = IO.buffer(out);
                }
                else
                {
                    fatal("Unable to open $ for writing", this);
                }
            }
            catch (Exception e)
            {
                problem(e, "Unable to open for writing").throwAsIllegalStateException();
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
        return UncheckedCode.of(() -> Bytes.bytes(Files.size(path))).orNull();
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
