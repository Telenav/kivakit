////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem.spi;

import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A virtual file service provider interface.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
public interface FileService extends FileSystemObjectService, WritableResource
{
    default java.io.File asJavaFile()
    {
        return null;
    }

    /**
     * @param that The file to rename to
     * @return True if the file was renamed
     */
    boolean renameTo(final FileService that);
}
