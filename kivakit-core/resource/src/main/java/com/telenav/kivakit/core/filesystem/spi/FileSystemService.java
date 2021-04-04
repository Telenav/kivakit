////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem.spi;

import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

@UmlClassDiagram(diagram = DiagramFileSystemService.class)
public interface FileSystemService
{
    boolean accepts(FilePath path);

    @UmlRelation(label = "provides")
    DiskService diskService(FilePath path);

    @UmlRelation(label = "provides")
    FileService fileService(FilePath path);

    @UmlRelation(label = "provides")
    FolderService folderService(FilePath path);
}
