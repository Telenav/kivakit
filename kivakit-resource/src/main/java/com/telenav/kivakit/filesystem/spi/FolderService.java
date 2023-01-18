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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_SERVICE_PROVIDER_INTERFACE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A service provider interface (SPI) for filesystem folders. In addition to the methods required by
 * {@link FileSystemObjectService}, this interface requires:
 *
 * <ul>
 *     <li>{@link #clear()} - Remove all files in this folder</li>
 *     <li>{@link #file(FileName)} - The file with the given name</li>
 *     <li>{@link #files()} - All files in this folder</li>
 *     <li>{@link #files(Matcher)} - All files matching the given matcher</li>
 *     <li>{@link #folder(Folder)} - The given sub-folder</li>
 *     <li>{@link #folder(FileName)} - The given sub-folder</li>
 *     <li>{@link #folders()} - All folders in this folder</li>
 *     <li>{@link #isEmpty()} - True if the folder is empty</li>
 *     <li>{@link #isWritable()} - True if the folder can be written to</li>
 *     <li>{@link #mkdirs()} - Creates this folder and all the parent folders as needed</li>
 *     <li>{@link #nestedFiles(Matcher)} - All nested files in this folder matching the given matcher</li>
 *     <li>{@link #nestedFolders(Matcher)} - All nested folders in this folder matching the given matcher</li>
 *     <li>{@link #renameTo(FolderService)} - Renames this folder to the given folder</li>
 *     <li>{@link #temporaryFile(FileName)} - A temporary file with the given base name</li>
 *     <li>{@link #temporaryFolder(FileName)} - A temporary folder with the given base name</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see FileSystemObjectService
 * @see FileSystemService
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_SERVICE_PROVIDER_INTERFACE)
public interface FolderService extends FileSystemObjectService
{
    /**
     * Clear the contents of this folder
     */
    default FolderService clear()
    {
        return unsupported();
    }

    /**
     * Returns the file service for the given file name
     */
    FileService file(@NotNull FileName name);

    /**
     * Returns the files in this folder
     */
    List<FileService> files();

    /**
     * Returns the files in this folder that match the matcher
     */
    default List<FileService> files(@NotNull Matcher<FilePath> matcher)
    {
        List<FileService> files = new ArrayList<>();
        for (var file : files())
        {
            if (matcher.matches(file.path()))
            {
                files.add(file);
            }
        }
        return files;
    }

    /**
     * Returns the named sub-folder in this folder
     */
    FolderService folder(@NotNull FileName folder);

    /**
     * Returns the named folder path within this folder
     */
    FolderService folder(@NotNull Folder folder);

    /**
     * Returns the folders in this folder
     */
    List<FolderService> folders();

    /**
     * Returns the folders in this folder that match the matcher
     */
    default List<FolderService> folders(@NotNull Matcher<FilePath> matcher)
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

    default boolean hasFiles()
    {
        return exists() && !files().isEmpty();
    }

    default boolean hasSubFolders()
    {
        return !folders().isEmpty();
    }

    /**
     * Returns true if the folder is empty
     */
    default boolean isEmpty()
    {
        return !hasFiles() && !hasSubFolders();
    }

    /**
     * Returns true if this folder can be written to
     */
    Boolean isWritable();

    /**
     * Unsupported
     */
    @SuppressWarnings("SpellCheckingInspection")
    default FolderService mkdirs()
    {
        return unsupported();
    }

    /**
     * Returns files in this folder that match the given matcher, recursively
     */
    List<FileService> nestedFiles(@NotNull Matcher<FilePath> matcher);

    /**
     * Returns files in this folder that match the given matcher, recursively
     */
    List<FolderService> nestedFolders(@NotNull Matcher<FilePath> matcher);

    /**
     * @param that The folder to rename this folder to
     * @return True if the folder was renamed
     */
    default boolean renameTo(@NotNull FolderService that)
    {
        return unsupported();
    }

    /**
     * @param baseName Base name of temporary file
     * @return A unique, existing temporary file in this folder
     */
    default FileService temporaryFile(@NotNull FileName baseName)
    {
        return unsupported();
    }

    /**
     * @param baseName Base name of temporary folder
     * @return A unique, existing temporary folder in this folder
     */
    default FolderService temporaryFolder(@NotNull FileName baseName)
    {
        return unsupported();
    }
}
