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

package com.telenav.kivakit.filesystem.local;

import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

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
    public static boolean isFile(final FilePath path)
    {
        return new java.io.File(path.join()).isFile();
    }

    private final java.io.File file;

    public LocalFile(final FilePath path)
    {
        super(path);
        file = new java.io.File(path.join());
    }

    public LocalFile(final java.io.File file)
    {
        this(FilePath.filePath(file));
    }

    public LocalFile(final LocalFile that)
    {
        super(that);
        file = that.file;
    }

    public LocalFile(final LocalFolder folder, final String name)
    {
        this(folder.path().withChild(name));
    }

    public LocalFile(final String path)
    {
        this(FilePath.parseFilePath(path));
    }

    @Override
    public java.io.File asJavaFile()
    {
        return file;
    }

    @Override
    public Bytes bytes()
    {
        return Bytes.bytes(file.length());
    }

    @Override
    public boolean chmod(final PosixFilePermission... permissions)
    {
        try
        {
            Files.setPosixFilePermissions(path().asJavaPath(), new HashSet<>(Arrays.asList(permissions)));
            return true;
        }
        catch (final Exception e)
        {
            return false;
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
    public boolean equals(final Object object)
    {
        if (object instanceof LocalFile)
        {
            final var that = (LocalFile) object;
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
    public boolean isWritable()
    {
        // If we can write to the file,
        if (file.canWrite())
        {
            // the file is writable
            return true;
        }

        // If the parent exists and is writable and the file doesn't exist
        // the file is writable because we can create it
        return parent().exists() && parent().isWritable() && !file.exists();
    }

    @Override
    public Time lastModified()
    {
        return Time.milliseconds(file.lastModified());
    }

    @Override
    public boolean lastModified(final Time time)
    {
        return file.setLastModified(time.asMilliseconds());
    }

    @Override
    public InputStream onOpenForReading()
    {
        try
        {
            return new BufferedInputStream(new FileInputStream(file));
        }
        catch (final FileNotFoundException e)
        {
            return fail(e, "Couldn't open file for reading: " + this);
        }
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        try
        {
            return new FileOutputStream(file);
        }
        catch (final FileNotFoundException e)
        {
            return fail(e, "Couldn't open file for writing: " + file);
        }
    }

    @Override
    public LocalFolder parent()
    {
        return new LocalFolder(path().absolute().parent());
    }

    @Override
    public FilePath path()
    {
        return (FilePath) super.path();
    }

    @Override
    public boolean renameTo(final FileService file)
    {
        if (isOnSameFileSystem(file))
        {
            return this.file.renameTo(((LocalFile) file.resolveService()).file);
        }
        fail("Cannot rename across filesystems");
        return false;
    }

    @Override
    public FolderService root()
    {
        return new LocalFolder(path().rootElement());
    }

    @Override
    public String toString()
    {
        return path().toString();
    }
}
