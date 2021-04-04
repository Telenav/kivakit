////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem.spi;

import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramFileSystemService.class)
public interface DiskService
{
    /**
     * @return Free space on this disk
     */
    Bytes free();

    /**
     * @return Percentage of this disk that is free
     */
    default Percent percentFree()
    {
        return free().percentOf(size());
    }

    /**
     * @return Percentage of this disk that is usable
     */
    default Percent percentUsable()
    {
        return usable().percentOf(size());
    }

    /**
     * @return Root folder on this disk
     */
    FolderService root();

    /**
     * @return Total size of this disk
     */
    Bytes size();

    /**
     * @return Usable space on this disk
     */
    Bytes usable();
}
