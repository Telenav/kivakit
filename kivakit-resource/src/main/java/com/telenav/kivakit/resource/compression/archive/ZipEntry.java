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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.string.KivaKitFormat;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.interfaces.io.Closeable;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceArchive;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * A zip entry in a {@link ZipArchive} that is a {@link WritableResource}.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #createdAt()}</li>
 *     <li>{@link #isWritable()}</li>
 *     <li>{@link #lastModified()}</li>
 *     <li>{@link #path()}</li>
 *     <li>{@link #sizeInBytes()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see ZipArchive
 */
@UmlClassDiagram(diagram = DiagramResourceArchive.class)
@UmlExcludeSuperTypes({ AutoCloseable.class })
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class ZipEntry extends BaseWritableResource implements Closeable
{
    private InputStream in;

    private OutputStream out;

    private final Path path;

    public ZipEntry(FileSystem filesystem, Path path)
    {
        this.path = filesystem.getPath(path.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        IO.close(this, in);
        in = null;

        IO.close(this, out);
        out = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @KivaKitFormat
    public Time createdAt()
    {
        return UncheckedCode.unchecked(() -> Time.epochMilliseconds(Files.readAttributes(path, BasicFileAttributes.class)
                .creationTime().toMillis())).orNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isWritable()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @KivaKitFormat
    @Override
    public Time lastModified()
    {
        return UncheckedCode.unchecked(() -> Time.epochMilliseconds(Files.getLastModifiedTime(path).toMillis())).orNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        if (in == null)
        {
            var in = UncheckedCode.unchecked(() -> Files.newInputStream(path)).orNull();
            if (in != null)
            {
                this.in = IO.bufferInput(in);
            }
            else
            {
                fatal("Unable to open $ for reading", this);
            }
        }
        return in;
    }

    /**
     * {@inheritDoc}
     */
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
                    this.out = IO.bufferOutput(out);
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

    /**
     * {@inheritDoc}
     */
    @KivaKitFormat
    @Override
    public FilePath path()
    {
        return FilePath.filePath(path);
    }

    /**
     * {@inheritDoc}
     */
    @KivaKitFormat
    @Override
    public Bytes sizeInBytes()
    {
        return UncheckedCode.unchecked(() -> Bytes.bytes(Files.size(path))).orNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
