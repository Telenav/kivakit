////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem.spi;

import com.telenav.kivakit.core.filesystem.loader.FileSystemServiceLoader;
import com.telenav.kivakit.core.kernel.interfaces.io.ByteSized;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.time.ModificationTimestamped;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.path.ResourcePathed;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A virtual file system interface.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlExcludeSuperTypes(ResourcePathed.class)
public interface FileSystemObjectService extends ByteSized, ModificationTimestamped, ResourcePathed
{
    /**
     * @return True if the permissions were changed
     */
    boolean chmod(PosixFilePermission... permissions);

    /**
     *
     */
    boolean delete();

    /**
     * @return The disk that this object is on
     */
    default DiskService diskService()
    {
        return unsupported();
    }

    /**
     * @return True if this folder exists
     */
    boolean exists();

    default FolderService folderService()
    {
        return FileSystemServiceLoader.fileSystem(path()).folderService(path());
    }

    /**
     * @return True if this is a file, false if it is a folder
     */
    default boolean isFile()
    {
        return !isFolder();
    }

    /**
     * @return True if this is a folder. If this is a folder, asFolder() will be successful.
     */
    boolean isFolder();

    default boolean isOnSameFileSystem(final FileSystemObjectService that)
    {
        final var thisService = FileSystemServiceLoader.fileSystem(path());
        final var thatService = FileSystemServiceLoader.fileSystem(that.path());
        return thisService.getClass().equals(thatService.getClass());
    }

    /**
     * @return The parent of this folder or null if none exists
     */
    FolderService parent();

    /**
     * @return The path to this object
     */
    @Override
    FilePath path();

    default FilePath relativePath(final FolderService folder)
    {
        final var fullName = Strings.ensureEndsWith(path().toString().replace("\\", "/"), "/");
        final var folderName = Strings.ensureEndsWith(folder.path().toString().replace("\\", "/"), "/");
        if (fullName.startsWith(folderName))
        {
            return FilePath.parseFilePath(fullName.substring(folderName.length()));
        }
        else
        {
            return FilePath.parseFilePath(fullName);
        }
    }

    default FileSystemObjectService resolveService()
    {
        return this;
    }

    /**
     * @return The root folder containing this object
     */
    FolderService rootFolderService();
}
