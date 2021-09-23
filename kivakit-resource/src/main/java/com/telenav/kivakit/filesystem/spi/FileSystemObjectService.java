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

import com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader;
import com.telenav.kivakit.kernel.interfaces.io.ByteSized;
import com.telenav.kivakit.kernel.interfaces.time.ChangedAt;
import com.telenav.kivakit.kernel.interfaces.time.CreatedAt;
import com.telenav.kivakit.kernel.interfaces.time.Modifiable;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.path.ResourcePathed;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A service provider interface (SPI) common to all filesystem objects. Implementers must provide:
 *
 * <ul>
 *     <li>{@link #chmod(PosixFilePermission...)} - Changes permissions of the object</li>
 *     <li>{@link #exists()} - True if the object exists</li>
 *     <li>{@link #delete()} - Deletes the object</li>
 *     <li>{@link #isFolder()} - Returns true if the object is a folder</li>
 *     <li>{@link #parent()} - The parent {@link FolderService} of the object, if any</li>
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
@LexakaiJavadoc(complete = true)
public interface FileSystemObjectService extends ByteSized, ChangedAt, CreatedAt, Modifiable, ResourcePathed
{
    /**
     * @return True if the permissions were changed
     */
    default boolean chmod(PosixFilePermission... permissions)
    {
        return unsupported();
    }

    /**
     * Deletes this filesystem object
     */
    default boolean delete()
    {
        return unsupported();
    }

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

    /**
     * @return The service provider for folders
     */
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
    FolderService root();
}
