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

package com.telenav.kivakit.core.filesystem.spi;

import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A service provider interface (SPI) for filesystem folders. In addition to the methods required by {@link
 * FileSystemObjectService}, this interface requires:
 *
 * <ul>
 *     <li>{@link #clear()} - Remove all files in this folder</li>
 *     <li>{@link #file(FileName)} - The file with the given name</li>
 *     <li>{@link #files()} - All files in this folder</li>
 *     <li>{@link #files(Matcher)} - All files matching the given matcher</li>
 *     <li>{@link #folder(Folder)} - The given subfolder</li>
 *     <li>{@link #folder(FileName)} - The given subfolder</li>
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
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@LexakaiJavadoc(complete = true)
public interface FolderService extends FileSystemObjectService
{
    /**
     * clear the contents of this folder
     */
    FolderService clear();

    /**
     *
     */
    FileService file(FileName name);

    /**
     * @return The files in this folder
     */
    List<? extends FileService> files();

    /**
     * @return The files in this folder that match the matcher
     */
    List<? extends FileService> files(final Matcher<FilePath> matcher);

    /**
     * @return The named sub-folder in this folder
     */
    FolderService folder(FileName folder);

    /**
     * @return The named folder path within this folder
     */
    FolderService folder(Folder folder);

    /**
     * @return The folders in this folder
     */
    Iterable<? extends FolderService> folders();

    /**
     * @return The folders in this folder that match the matcher
     */
    List<? extends FolderService> folders(final Matcher<FilePath> matcher);

    /**
     * @return true if the folder is empty
     */
    boolean isEmpty();

    /**
     * @return True if this folder can be written to
     */
    boolean isWritable();

    /**
     * @return True if the folder was created, along with all necessary parent folders.
     */
    FolderService mkdirs();

    /**
     * @return Files in this folder that match the given matcher, recursively
     */
    List<? extends FileService> nestedFiles(final Matcher<FilePath> matcher);

    /**
     * @return Files in this folder that match the given matcher, recursively
     */
    List<? extends FolderService> nestedFolders(final Matcher<FilePath> matcher);

    /**
     * @param that The folder to rename this folder to
     * @return True if the folder was renamed
     */
    boolean renameTo(final FolderService that);

    /**
     * @param baseName Base name of temporary file
     * @return A unique, existing temporary file in this folder
     */
    FileService temporaryFile(final FileName baseName);

    /**
     * @param baseName Base name of temporary folder
     * @return A unique, existing temporary folder in this folder
     */
    FolderService temporaryFolder(final FileName baseName);
}
