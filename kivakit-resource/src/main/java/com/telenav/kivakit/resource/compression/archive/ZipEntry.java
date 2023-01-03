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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.interfaces.io.Closeable;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceArchive;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.code.UncheckedCode.unchecked;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.io.IO.buffer;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.filesystem.FilePath.filePath;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.getLastModifiedTime;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Files.readAttributes;
import static java.nio.file.Files.size;
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
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ZipEntry extends BaseWritableResource implements Closeable
{
    private InputStream in;

    private OutputStream out;

    private final Path path;

    public ZipEntry(@NotNull FileSystem filesystem,
                    @NotNull Path path)
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
    @FormatProperty
    public Time createdAt()
    {
        return unchecked(() -> epochMilliseconds(readAttributes(path, BasicFileAttributes.class)
            .creationTime().toMillis())).orNull();
    }

    @Override
    @FormatProperty
    public FileName fileName()
    {
        return super.fileName();
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
    @FormatProperty
    @Override
    public Time lastModified()
    {
        return unchecked(() -> epochMilliseconds(getLastModifiedTime(path).toMillis())).orNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        if (in == null)
        {
            var in = unchecked(() -> newInputStream(path)).orNull();
            if (in != null)
            {
                this.in = buffer(in);
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
    public OutputStream onOpenForWriting(WriteMode mode)
    {
        ensure(mode == OVERWRITE);
        if (out == null)
        {
            OutputStream out;
            try
            {
                var parent = path.getParent();
                if (parent != null)
                {
                    createDirectories(parent);
                }
                out = newOutputStream(path, CREATE, WRITE);
                if (out != null)
                {
                    this.out = buffer(out);
                }
                else
                {
                    fatal("Unable to open $ for writing", this);
                }
            }
            catch (Exception e)
            {
                problem(e, "Unable to open for writing").throwMessage();
            }
        }
        return out;
    }

    /**
     * {@inheritDoc}
     */
    @FormatProperty
    @Override
    public FilePath path()
    {
        return filePath(path);
    }

    /**
     * {@inheritDoc}
     */
    @FormatProperty
    @Override
    public Bytes sizeInBytes()
    {
        return unchecked(() -> bytes(size(path))).orNull();
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
