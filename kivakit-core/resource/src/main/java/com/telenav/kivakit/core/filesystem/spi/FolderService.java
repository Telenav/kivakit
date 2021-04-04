////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem.spi;

import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A virtual folder interface.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
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
