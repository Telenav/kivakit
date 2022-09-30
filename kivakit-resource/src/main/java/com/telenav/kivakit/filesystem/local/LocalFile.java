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

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemService;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

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

/**
 * Implementation of {@link FileService} provider interface for the local filesystem.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlNotPublicApi
@LexakaiJavadoc(complete = true)
public class LocalFile extends BaseWritableResource implements FileService
{
    public static boolean isFile(FilePath path)
    {
        return new java.io.File(path.join()).isFile();
    }

    private final java.io.File file;

    public LocalFile(FilePath path)
    {
        super(path.withoutFileScheme());
        file = new java.io.File(path.join());
    }

    public LocalFile(java.io.File file)
    {
        this(FilePath.filePath(file));
    }

    public LocalFile(LocalFile that)
    {
        super(that);
        file = that.file;
    }

    public LocalFile(LocalFolder folder, String name)
    {
        this(folder.path().withoutTrailingSlash().withChild(name));
    }

    public LocalFile(String path)
    {
        this(FilePath.parseFilePath(Listener.consoleListener(), path));
    }

    @Override
    public java.io.File asJavaFile()
    {
        return file;
    }

    @Override
    public boolean chmod(PosixFilePermission... permissions)
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

    @Override
    public Time createdAt()
    {
        try
        {
            FileTime creationTime = (FileTime) Files.getAttribute(path().asJavaPath(), "creationTime");
            return Time.epochMilliseconds(creationTime.toMillis());
        }
        catch (IOException e)
        {
            problem(e, "Unable to determine file creation time: $", path());
            return null;
        }
    }

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

    @Override
    public DiskService diskService()
    {
        return root().diskService();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof LocalFile)
        {
            var that = (LocalFile) object;
            return file.equals(that.file);
        }
        return false;
    }

    @Override
    public boolean exists()
    {
        return file.exists();
    }

    @Override
    public LocalFolder folderService()
    {
        return new LocalFolder(file);
    }

    @Override
    public int hashCode()
    {
        return file.hashCode();
    }

    @Override
    public boolean isFolder()
    {
        return file.isDirectory();
    }

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

    @Override
    public boolean lastModified(Time time)
    {
        return file.setLastModified(time.milliseconds());
    }

    @Override
    public Time modifiedAt()
    {
        return Time.epochMilliseconds(file.lastModified());
    }

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

    @Override
    public OutputStream onOpenForWriting()
    {
        try
        {
            return new FileOutputStream(file);
        }
        catch (FileNotFoundException e)
        {
            return fatal(e, "Couldn't open file for writing: " + file);
        }
    }

    @Override
    public LocalFolder parentService()
    {
        return new LocalFolder(path().asAbsolute().parent());
    }

    @Override
    public FilePath path()
    {
        return (FilePath) super.path();
    }

    @Override
    public boolean renameTo(FileService file)
    {
        if (isOnSameFileSystem(file))
        {
            return this.file.renameTo(((LocalFile) file.resolveService()).file);
        }
        return fatal("Cannot rename across filesystems");
    }

    @Override
    public FolderService root()
    {
        return new LocalFolder(path().rootElement());
    }

    @Override
    public Bytes sizeInBytes()
    {
        return Bytes.bytes(file.length());
    }

    @Override
    public String toString()
    {
        return path().toString();
    }
}
