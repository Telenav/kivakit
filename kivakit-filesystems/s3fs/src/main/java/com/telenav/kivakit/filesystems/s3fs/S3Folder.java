////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.filesystem.spi.FolderService;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.threading.locks.Monitor;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.filesystems.s3fs.project.lexakai.diagrams.DiagramS3;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;

import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

@UmlClassDiagram(diagram = DiagramS3.class)
public class S3Folder extends S3FileSystemObject implements FolderService
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Monitor LOCK = new Monitor();

    // In the S3 world, there no folder physically, so we created a metadata file to
    // represents its existence
    private static final FileName METADATA = new FileName(".metadata");

    public S3Folder(final FilePath path)
    {
        super(path, true);
    }

    public S3Folder(final String path)
    {
        this(FilePath.parseFilePath(path));
    }

    @Override
    public Bytes bytes()
    {
        return null;
    }

    @Override
    public boolean chmod(final PosixFilePermission... permissions)
    {
        return unsupported();
    }

    @Override
    public S3Folder clear()
    {
        for (final var folder : folders())
        {
            folder.clear();
            folder.delete();
        }

        for (final var file : files())
        {
            if (!isMetadata(file.fileName()))
            {
                file.delete();
            }
        }
        return this;
    }

    @Override
    public boolean delete()
    {
        if (!isEmpty())
        {
            LOGGER.warning("Can't delete a non-empty folder of " + this);
            return false;
        }

        // delete the meta data file
        final var metaFile = metadataFile();
        if (metaFile.exists())
        {
            metaFile.delete();
        }

        // delete this s3 object
        return super.delete();
    }

    @Override
    public void ensureExists()
    {
        if (!exists())
        {
            mkdirs();
        }
    }

    @Override
    public boolean exists()
    {
        return super.exists() || metadataFile().exists() || !isEmpty();
    }

    @Override
    public S3File file(final FileName fileName)
    {
        final var path = path();
        path.withChild(fileName.name());
        return new S3File(path);
    }

    @Override
    public List<S3File> files()
    {
        final var request = ListObjectsRequest.builder()
                .bucket(bucket())
                .build();

        final var response = client().listObjects(request);

        final List<S3File> files = new ArrayList<>();
        for (final var object : response.contents())
        {
            final var path = S3FileSystemObject.path(scheme(), bucket(), object.key());
            files.add(new S3File(path));
        }
        return files;
    }

    @Override
    public List<? extends FileService> files(final Matcher<FilePath> matcher)
    {
        final List<S3File> files = new ArrayList<>();
        for (final var file : files())
        {
            if (matcher.matches(file.path()))
            {
                files.add(file);
            }
        }
        return files;
    }

    @Override
    public S3Folder folder(final FileName name)
    {
        return new S3Folder(FilePath.parseFilePath(child(name).toString()));
    }

    @Override
    public S3Folder folder(final Folder folder)
    {
        return new S3Folder(FilePath.parseFilePath(child(folder).toString()));
    }

    @Override
    public List<S3Folder> folders()
    {
        final var request = ListBucketsRequest.builder().build();
        final var response = client().listBuckets(request);

        final List<S3Folder> folders = new ArrayList<>();
        for (final var bucket : response.buckets())
        {
            final var path = S3FileSystemObject.path(scheme(), bucket.name(), "");
            folders.add(new S3Folder(path));
        }
        return folders;
    }

    @Override
    public List<? extends FolderService> folders(final Matcher<FilePath> matcher)
    {
        return unsupported();
    }

    public boolean hasFiles()
    {
        return exists() && files().iterator().hasNext();
    }

    public boolean hasSubFolders()
    {
        return exists() && folders().iterator().hasNext();
    }

    @Override
    public boolean isEmpty()
    {
        final Iterable<S3Folder> folders = folders();
        if (folders != null && folders().iterator().hasNext())
        {
            return false;
        }
        for (final var file : files())
        {
            if (!file.fileName().equals(METADATA))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isWritable()
    {
        return false;
    }

    @Override
    public S3Folder mkdirs()
    {
        var folder = this;
        while (folder != null && !folder.equals(rootFolderService()))
        {
            mkdir(folder);
            folder = folder.parent();
        }
        return this;
    }

    @Override
    public List<? extends FileService> nestedFiles(final Matcher<FilePath> matcher)
    {
        final List<S3File> files = new ArrayList<>();
        for (final var file : nestedFiles(this, new ArrayList<>()))
        {
            if (matcher.matches(file.path()))
            {
                files.add(file);
            }
        }
        return files;
    }

    @Override
    public List<? extends FolderService> nestedFolders(final Matcher<FilePath> matcher)
    {
        final List<S3Folder> folders = new ArrayList<>();
        for (final var at : nestedFolders(this, new ArrayList<>()))
        {
            if (matcher.matches(at.path()))
            {
                folders.add(at);
            }
        }
        return folders;
    }

    @Override
    public boolean renameTo(final FolderService that)
    {
        if (isOnSameFileSystem(that))
        {
            return renameTo((S3Folder) that.resolveService());
        }
        fail("Cannot rename $ to $ across filesystems", this, that);
        return false;
    }

    public boolean renameTo(final S3Folder that)
    {
        if (exists() && canRenameTo(that))
        {
            for (final var file : files())
            {
                file.renameTo(that.file(file.fileName()));
            }
            for (final var folder : folders())
            {
                folder.renameTo(that.folder(folder.fileName()));
            }
            metadataFile().renameTo(that.metadataFile());
            return true;
        }
        return false;
    }

    @Override
    public S3Folder rootFolderService()
    {
        final var path = path(scheme(), bucket(), "");
        return new S3Folder(path);
    }

    @Override
    public S3File temporaryFile(final FileName baseName)
    {
        synchronized (LOCK)
        {
            var sequenceNumber = 0;
            S3File file;
            do
            {
                file = file(baseName.withSuffix("-" + sequenceNumber + ".tmp"));
                sequenceNumber++;
            }
            while (file.exists());
            return file;
        }
    }

    @Override
    public S3Folder temporaryFolder(final FileName baseName)
    {
        synchronized (LOCK)
        {
            var sequenceNumber = 0;
            S3Folder folder;
            do
            {
                folder = folder(baseName.withSuffix("-" + sequenceNumber + ".tmp"));
                sequenceNumber++;
            }
            while (folder.exists());
            folder.mkdirs();
            return folder;
        }
    }

    private FilePath child(final FileName child)
    {
        final var path = path();
        path.withChild(child.name());
        return path;
    }

    private FilePath child(final Folder folder)
    {
        final var path = path();
        path.withChild(folder.toString());
        return path;
    }

    private boolean isMetadata(final FileName fileName)
    {
        return fileName.equals(METADATA);
    }

    private S3File metadataFile()
    {
        return file(METADATA);
    }

    private void mkdir(final S3Folder folder)
    {
        final var file = folder.file(METADATA);
        if (!file.exists())
        {
            // Create a place holder for every folder to make sure it's a folder
            // conceptually
            file.write(Time.now().toString());
        }
    }

    private List<S3File> nestedFiles(final S3Folder folder, final List<S3File> files)
    {
        files.addAll(folder.files());
        for (final var at : folders())
        {
            nestedFiles(at, files);
        }
        return files;
    }

    private List<S3Folder> nestedFolders(final S3Folder folder, final List<S3Folder> folders)
    {
        folders.add(this);
        for (final var at : folder.folders())
        {
            nestedFolders(at, folders);
        }
        return folders;
    }
}
