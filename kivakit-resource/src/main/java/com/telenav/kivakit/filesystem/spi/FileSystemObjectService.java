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

package com.telenav.kivakit.filesystem.spi;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.time.CreatedAt;
import com.telenav.kivakit.core.time.Modifiable;
import com.telenav.kivakit.core.time.ModifiedAt;
import com.telenav.kivakit.core.value.count.ByteSized;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader;
import com.telenav.kivakit.resource.Deletable;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiType.API_SERVICE_PROVIDER;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A service provider interface (SPI) common to all filesystem objects. Implementers must provide:
 *
 * <ul>
 *     <li>{@link #chmod(PosixFilePermission...)} - Changes permissions of the object</li>
 *     <li>{@link #exists()} - True if the object exists</li>
 *     <li>{@link #delete()} - Deletes the object</li>
 *     <li>{@link #isFolder()} - Returns true if the object is a folder</li>
 *     <li>{@link #parentService()} - The parent {@link FolderService} of the object, if any</li>
 *     <li>{@link #path()} - The path to the object on the filesystem</li>
 *     <li>{@link #root()} - The root folder</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see FileService
 * @see FolderService
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlExcludeSuperTypes(ResourcePathed.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE,
            type = API_SERVICE_PROVIDER)
public interface FileSystemObjectService extends
        Repeater,
        ByteSized,
        ModifiedAt,
        CreatedAt,
        Modifiable,
        ResourcePathed,
        Deletable
{
    /**
     * Returns true if the permissions were changed
     */
    default boolean chmod(@NotNull PosixFilePermission... permissions)
    {
        return unsupported();
    }

    /**
     * Returns the disk that this object is on
     */
    default DiskService diskService()
    {
        return unsupported();
    }

    /**
     * Returns true if this folder exists
     */
    boolean exists();

    default FileSystemService fileSystemService(@NotNull FilePath path)
    {
        return FileSystemServiceLoader.fileSystem(Listener.throwingListener(), path);
    }

    /**
     * Returns the service provider for folders
     */
    default FolderService folderService()
    {
        return fileSystemService(path()).folderService(path());
    }

    /**
     * Returns true if this is a file, false if it is a folder
     */
    default boolean isFile()
    {
        return !isFolder();
    }

    /**
     * Returns true if this is a folder. If this is a folder, asFolder() will be successful.
     */
    boolean isFolder();

    default boolean isOnSameFileSystem(@NotNull FileSystemObjectService that)
    {
        var thisService = fileSystemService(path());
        var thatService = fileSystemService(that.path());
        return thisService.getClass().equals(thatService.getClass());
    }

    /**
     * Returns true if this folder is remote
     */
    default boolean isRemote()
    {
        return false;
    }

    /**
     * Returns the parent of this folder or null if none exists
     */
    FolderService parentService();

    /**
     * Returns the path to this object
     */
    @Override
    FilePath path();

    default FilePath relativePath(@NotNull FolderService folderService)
    {
        var fullName = Strings.ensureEndsWith(path().toString().replace("\\", "/"), "/");
        var folderName = Strings.ensureEndsWith(folderService.path().toString().replace("\\", "/"), "/");
        if (fullName.startsWith(folderName))
        {
            return FilePath.parseFilePath(this, fullName.substring(folderName.length()));
        }
        else
        {
            return FilePath.parseFilePath(this, fullName);
        }
    }

    default FileSystemObjectService resolveService()
    {
        return this;
    }

    /**
     * Returns the root folder containing this object
     */
    FolderService root();
}
