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

package com.telenav.kivakit.filesystem.local;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemService;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.consoleListener;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.filesystem.FilePath.filePath;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.resource.WriteMode.APPEND;

/**
 * Implementation of {@link FileService} provider interface for the local filesystem.
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlNotPublicApi
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class LocalFile extends BaseWritableResource implements FileService
{
    /** The underlying Java file */
    private final java.io.File file;

    public LocalFile(@NotNull FilePath path)
    {
        super(path.withoutFileScheme());
        file = new java.io.File(path.join());
    }

    public LocalFile(@NotNull java.io.File file)
    {
        this(filePath(file));
    }

    public LocalFile(@NotNull LocalFile that)
    {
        super(that);
        file = that.file;
    }

    public LocalFile(@NotNull LocalFolder folder, @NotNull String name)
    {
        this(folder.path().withoutTrailingSlash().withChild(name));
    }

    public LocalFile(@NotNull String path)
    {
        this(parseFilePath(consoleListener(), path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.io.File asJavaFile()
    {
        return file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean chmod(@NotNull PosixFilePermission... permissions)
    {
        try
        {
            Files.setPosixFilePermissions(path().asJavaPath(), new HashSet<>(Arrays.asList(permissions)));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time createdAt()
    {
        try
        {
            FileTime creationTime = (FileTime) Files.getAttribute(path().asJavaPath(), "creationTime");
            return epochMilliseconds(creationTime.toMillis());
        }
        catch (IOException e)
        {
            problem(e, "Unable to determine file creation time: $", path());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean delete()
    {
        if (exists())
        {
            return file.delete();
        }
        else
        {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiskService diskService()
    {
        return root().diskService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof LocalFile that)
        {
            return file.equals(that.file);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists()
    {
        return file.exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalFolder folderService()
    {
        return new LocalFolder(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return file.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFolder()
    {
        return file.isDirectory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isWritable()
    {
        // If we can write to the file,
        if (file.canWrite())
        {
            // the file is writable
            return true;
        }

        // If the parent exists and is writable and the file doesn't exist
        // the file is writable because we can create it
        return parentService().exists() && parentService().isWritable() && !file.exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean lastModified(@NotNull Time time)
    {
        return file.setLastModified(time.milliseconds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time lastModified()
    {
        return epochMilliseconds(file.lastModified());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        try
        {
            return new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            return fatal(e, "Couldn't open file for reading: " + this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream onOpenForWriting(WriteMode mode)
    {
        mode.ensureAllowed(file);
        try
        {
            return new FileOutputStream(file, mode == APPEND);
        }
        catch (FileNotFoundException e)
        {
            return fatal(e, "Couldn't open file for writing: " + file);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalFolder parentService()
    {
        return new LocalFolder(path().asAbsolute().parent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath path()
    {
        return (FilePath) super.path();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean renameTo(@NotNull FileService file)
    {
        if (isOnSameFileSystem(file))
        {
            return this.file.renameTo(((LocalFile) file.resolveService()).file);
        }
        return fatal("Cannot rename across filesystems");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FolderService root()
    {
        return new LocalFolder(path().rootElement());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes sizeInBytes()
    {
        return bytes(file.length());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return path().toString();
    }
}
