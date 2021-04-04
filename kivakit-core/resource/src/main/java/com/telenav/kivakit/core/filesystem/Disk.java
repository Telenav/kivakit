////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFolder;
import com.telenav.kivakit.core.filesystem.spi.DiskService;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
public class Disk
{
    private final DiskService disk;

    @UmlExcludeMember
    public Disk(final DiskService disk)
    {
        this.disk = disk;
    }

    public Bytes free()
    {
        return disk.free();
    }

    public Percent percentFree()
    {
        return disk.percentFree();
    }

    public Percent percentUsable()
    {
        return disk.percentUsable();
    }

    public Folder root()
    {
        return new Folder(disk.root());
    }

    public Bytes size()
    {
        return disk.size();
    }

    @Override
    public String toString()
    {
        return disk.toString();
    }

    public Bytes usable()
    {
        return disk.usable();
    }
}
