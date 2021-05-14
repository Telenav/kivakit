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

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.threading.locks.Monitor;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * Implementation of {@link FolderService} provider interface for the local filesystem.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlNotPublicApi
@LexakaiJavadoc(complete = true)
public class LocalFolder implements FolderService
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    // Monitor for serializing the creation of temporary files
    private static final Monitor temporaryLock = new Monitor();

    public static LocalFolder home()
    {
        return new LocalFolder(System.getProperty("user.home"));
    }

    private final java.io.File file;

    private final FilePath path;

    public LocalFolder(final FilePath path)
    {
        this.path = path;
        file = new java.io.File(this.path.toString());
    }

    public LocalFolder(final java.io.File file)
    {
        this(FilePath.filePath(file));
    }

    public LocalFolder(final LocalFolder that)
    {
        file = that.file;
        path = that.path;
    }

    public LocalFolder(final String path)
    {
        this(FilePath.parseFilePath(path));
    }

    public LocalFolder(final URI uri)
    {
        this(new java.io.File(uri));
    }

    public LocalFolder(final URL url) throws URISyntaxException
    {
        this(url.toURI());
    }

    public LocalFolder asAbsolute()
    {
        return new LocalFolder(path().absolute());
    }

    public URI asUri()
    {
        return file.toURI();
    }

    public URL asUrl()
    {
        try
        {
            return asUri().toURL();
        }
        catch (final MalformedURLException e)
        {
            return null;
        }
    }

    @Override
    public Bytes bytes()
    {
        return null;
    }

    @Override
    public boolean chmod(final PosixFilePermission... permissions)
    {
        try
        {
            Files.setPosixFilePermissions(path.asJavaPath(), new HashSet<>(Arrays.asList(permissions)));
            return true;
        }
        catch (final Exception e)
        {
            return false;
        }
    }

    @Override
    public FolderService clear()
    {
        for (final var folder : folders())
        {
            folder.clear();
            folder.delete();
        }

        for (final var file : files())
        {
            file.delete();
        }
        return this;
    }

    /**
     * Delete a folder if it is empty
     */
    @Override
    public synchronized boolean delete()
    {
        if (exists())
        {
            if (isEmpty())
            {
                if (!file.delete())
                {
                    if (exists())
                    {
                        LOGGER.warning("Couldn't delete folder ${debug}", this);
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                fail("Cannot delete a folder which is not empty. Call clear() first!");
                return false;
            }
        }
        return false;
    }

    @Override
    public LocalDisk diskService()
    {
        return new LocalDisk(this);
    }

    public LocalFolder ensureExists()
    {
        if (!exists())
        {
            mkdirs();
        }
        return this;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof LocalFolder)
        {
            final var that = (LocalFolder) object;
            return path.join().equals(that.path.join());
        }
        return false;
    }

    @Override
    public synchronized boolean exists()
    {
        return file.exists();
    }

    @Override
    public LocalFile file(final FileName name)
    {
        return file(name.name());
    }

    public LocalFile file(final FilePath path)
    {
        return file(path.toString());
    }

    public LocalFile file(final String name)
    {
        return new LocalFile(this, name);
    }

    @Override
    public FileName fileName()
    {
        return path.fileName();
    }

    @Override
    public synchronized List<LocalFile> files()
    {
        final List<LocalFile> files = new ArrayList<>();
        if (exists())
        {
            if (file != null)
            {
                final var list = file.listFiles();
                if (list != null)
                {
                    for (final var file : list)
                    {
                        final var path = FilePath.filePath(file);
                        if (!isFolder(path))
                        {
                            final var result = new LocalFile(file);
                            files.add(result);
                        }
                    }
                }
            }
        }
        return files;
    }

    @Override
    public List<FileService> files(final Matcher<FilePath> matcher)
    {
        final List<FileService> files = new ArrayList<>();
        for (final FileService file : files())
        {
            if (matcher.matches(file.path()))
            {
                files.add(file);
            }
        }
        return files;
    }

    @Override
    public LocalFolder folder(final FileName name)
    {
        return new LocalFolder(path.withChild(name.toString()));
    }

    @Override
    public LocalFolder folder(final Folder folder)
    {
        return new LocalFolder(path.withChild(folder.toString()));
    }

    public LocalFolder folder(final LocalFolder child)
    {
        return folder(child.toString());
    }

    public LocalFolder folder(final String child)
    {
        return new LocalFolder(path.withChild(child));
    }

    @Override
    public synchronized List<FolderService> folders()
    {
        final List<FolderService> folders = new ArrayList<>();
        if (file != null)
        {
            final var files = file.listFiles();
            if (files != null)
            {
                for (final var file : files)
                {
                    if (!file.getName().startsWith(".") && file.isDirectory())
                    {
                        folders.add(new LocalFolder(file));
                    }
                }
            }
        }
        return folders;
    }

    @Override
    public List<FolderService> folders(final Matcher<FilePath> matcher)
    {
        final List<FolderService> folders = new ArrayList<>();
        for (final var folder : folders())
        {
            if (matcher.matches(folder.path()))
            {
                folders.add(folder);
            }
        }
        return folders;
    }

    @Override
    public int hashCode()
    {
        return path.join().hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        return files().isEmpty();
    }

    @Override
    public boolean isFolder()
    {
        return file.isDirectory();
    }

    @Override
    public boolean isWritable()
    {
        return file.canWrite();
    }

    @Override
    public Time lastModified()
    {
        return Time.milliseconds(file.lastModified());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public LocalFolder mkdirs()
    {
        file.mkdirs();
        return this;
    }

    @Override
    public List<FileService> nestedFiles(final Matcher<FilePath> matcher)
    {
        final var files = files(matcher);
        for (final var folder : folders())
        {
            files.addAll(folder.nestedFiles(matcher));
        }
        return files;
    }

    @Override
    public List<? extends FolderService> nestedFolders(final Matcher<FilePath> matcher)
    {
        final var folders = folders(matcher);
        for (final var folder : folders())
        {
            folders.addAll(folder.nestedFolders(matcher));
        }
        return folders;
    }

    @Override
    public LocalFolder parent()
    {
        return new LocalFolder(path().absolute().parent());
    }

    @Override
    public FilePath path()
    {
        return path;
    }

    @Override
    public boolean renameTo(final FolderService that)
    {
        if (isOnSameFileSystem(that))
        {
            return file.renameTo(((LocalFolder) that.resolveService()).file);
        }
        fail("Cannot rename across filesystems");
        return false;
    }

    @Override
    public FolderService root()
    {
        return new LocalFolder(path.rootElement());
    }

    @Override
    public LocalFile temporaryFile(final FileName baseName)
    {
        synchronized (temporaryLock)
        {
            var sequenceNumber = 0;
            LocalFile file;
            do
            {
                file = file(baseName + "-" + sequenceNumber + ".tmp");
                sequenceNumber++;
            }
            while (file.exists());
            file.writer().save("");
            return file;
        }
    }

    @Override
    public LocalFolder temporaryFolder(final FileName baseName)
    {
        synchronized (temporaryLock)
        {
            var sequenceNumber = 0;
            LocalFolder folder;
            do
            {
                folder = folder(baseName + "-" + sequenceNumber);
                sequenceNumber++;
            }
            while (folder.exists());
            folder.mkdirs();
            return folder;
        }
    }

    @Override
    public String toString()
    {
        return path.join();
    }

    private static boolean isFolder(final FilePath path)
    {
        return new java.io.File(path.join()).isDirectory();
    }

    java.io.File asJavaFile()
    {
        return file;
    }
}
