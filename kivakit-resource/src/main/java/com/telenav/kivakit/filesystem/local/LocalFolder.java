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
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.thread.Monitor;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * Implementation of {@link FolderService} provider interface for the local filesystem.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlNotPublicApi
@LexakaiJavadoc(complete = true)
public class LocalFolder extends BaseRepeater implements FolderService
{
    // Monitor for serializing the creation of temporary files
    private static final Monitor temporaryLock = new Monitor();

    public static LocalFolder home()
    {
        return new LocalFolder(System.getProperty("user.home"));
    }

    private final java.io.File file;

    private final FilePath path;

    public LocalFolder(FilePath path)
    {
        this.path = path;
        file = new java.io.File(this.path.toString());
    }

    public LocalFolder(java.io.File file)
    {
        this(FilePath.filePath(file));
    }

    public LocalFolder(LocalFolder that)
    {
        file = that.file;
        path = that.path;
    }

    public LocalFolder(String path)
    {
        this(FilePath.parseFilePath(Listener.consoleListener(), path));
    }

    public LocalFolder(URI uri)
    {
        this(new java.io.File(uri));
    }

    public LocalFolder(URL url) throws URISyntaxException
    {
        this(url.toURI());
    }

    public LocalFolder asAbsolute()
    {
        return new LocalFolder(path().absolute());
    }

    @Override
    public java.io.File asJavaFile()
    {
        return file;
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
        catch (MalformedURLException e)
        {
            return null;
        }
    }

    @Override
    public boolean chmod(PosixFilePermission... permissions)
    {
        try
        {
            Files.setPosixFilePermissions(path.asJavaPath(), new HashSet<>(Arrays.asList(permissions)));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public FolderService clear()
    {
        for (var folder : folders())
        {
            folder.clear();
            folder.delete();
        }

        for (var file : files())
        {
            file.delete();
        }
        return this;
    }

    @Override
    public Time createdAt()
    {
        try
        {
            return Time.epochMilliseconds(Files.readAttributes(path().asJavaPath(), BasicFileAttributes.class)
                    .creationTime()
                    .toMillis());
        }
        catch (IOException e)
        {
            problem(e, "Unable to determine file creation time: $", path());
            return null;
        }
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
                        warning("Couldn't delete folder ${debug}", this);
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
    public boolean equals(Object object)
    {
        if (object instanceof LocalFolder)
        {
            var that = (LocalFolder) object;
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
    public LocalFile file(FileName name)
    {
        return file(name.name());
    }

    public LocalFile file(FilePath path)
    {
        return file(path.toString());
    }

    public LocalFile file(String name)
    {
        return new LocalFile(this, name);
    }

    @Override
    public FileName fileName()
    {
        return path.fileName();
    }

    @Override
    public synchronized List<FileService> files()
    {
        var files = new ArrayList<FileService>();
        if (exists())
        {
            if (file != null)
            {
                var list = file.listFiles();
                if (list != null)
                {
                    for (var file : list)
                    {
                        var path = FilePath.filePath(file).withoutFileScheme();
                        if (!isFolder(path))
                        {
                            files.add(new LocalFile(file));
                        }
                    }
                }
            }
        }
        return files;
    }

    @Override
    public List<FileService> files(Matcher<FilePath> matcher)
    {
        List<FileService> files = new ArrayList<>();
        for (FileService file : files())
        {
            if (matcher.matches(file.path()))
            {
                files.add(file);
            }
        }
        return files;
    }

    @Override
    public LocalFolder folder(FileName name)
    {
        return new LocalFolder(path.withChild(name.toString()));
    }

    @Override
    public LocalFolder folder(Folder folder)
    {
        return new LocalFolder(path.withChild(folder.toString()));
    }

    public LocalFolder folder(LocalFolder child)
    {
        return folder(child.toString());
    }

    public LocalFolder folder(String child)
    {
        return new LocalFolder(path.withChild(child));
    }

    @Override
    public synchronized List<FolderService> folders()
    {
        List<FolderService> folders = new ArrayList<>();
        if (file != null)
        {
            var files = file.listFiles();
            if (files != null)
            {
                for (var file : files)
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
    public List<FolderService> folders(Matcher<FilePath> matcher)
    {
        List<FolderService> folders = new ArrayList<>();
        for (var folder : folders())
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
    public Boolean isWritable()
    {
        return file.canWrite();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public LocalFolder mkdirs()
    {
        file.mkdirs();
        return this;
    }

    @Override
    public Time modifiedAt()
    {
        return Time.epochMilliseconds(file.lastModified());
    }

    @Override
    public List<FileService> nestedFiles(Matcher<FilePath> matcher)
    {
        var files = files(matcher);
        for (var folder : folders())
        {
            files.addAll(folder.nestedFiles(matcher));
        }
        return files;
    }

    @Override
    public List<FolderService> nestedFolders(Matcher<FilePath> matcher)
    {
        var folders = folders(matcher);
        for (var folder : folders())
        {
            folders.addAll(folder.nestedFolders(matcher));
        }
        return folders;
    }

    @Override
    public LocalFolder parentService()
    {
        return new LocalFolder(path().absolute().parent());
    }

    @Override
    public FilePath path()
    {
        return path.withoutFileScheme();
    }

    @Override
    public boolean renameTo(FolderService that)
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
    public Bytes sizeInBytes()
    {
        return null;
    }

    @Override
    public LocalFile temporaryFile(FileName baseName)
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
    public LocalFolder temporaryFolder(FileName baseName)
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

    private static boolean isFolder(FilePath path)
    {
        return new java.io.File(path.join()).isDirectory();
    }
}
