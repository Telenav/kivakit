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

package com.telenav.kivakit.core.resource.compression.archive;

import com.telenav.kivakit.core.kernel.interfaces.code.CheckedCode;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceArchive;
import com.telenav.kivakit.core.resource.writing.BaseWritableResource;
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

    @KivaKitIncludeProperty
    @Override
    public Bytes bytes()
    {
        return CheckedCode.of(() -> Bytes.bytes(Files.size(path))).or(null);
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
    public boolean isWritable()
    {
        return false;
    }

    @KivaKitIncludeProperty
    @Override
    public Time lastModified()
    {
        return CheckedCode.of(() -> Time.milliseconds(Files.getLastModifiedTime(path).toMillis())).or(null);
    }

    @Override
    public InputStream onOpenForReading()
    {
        if (in == null)
        {
            final var in = CheckedCode.of(() -> Files.newInputStream(path)).or(null);
            if (in != null)
            {
                this.in = IO.buffer(in);
            }
        }
        return in;
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        if (out == null)
        {
            final var out = CheckedCode.of(() -> Files.newOutputStream(path, CREATE)).or(null);
            if (out != null)
            {
                this.out = IO.buffer(out);
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

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
