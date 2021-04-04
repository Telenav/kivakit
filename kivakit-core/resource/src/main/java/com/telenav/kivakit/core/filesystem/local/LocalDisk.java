////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem.local;

import com.telenav.kivakit.core.filesystem.spi.DiskService;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlNotPublicApi
public class LocalDisk implements DiskService
{
    private final LocalFolder root;

    public LocalDisk(final LocalFolder folder)
    {
        // Go up the folder hierarchy until we find a partition mount point
        var at = folder;
        while (at.asJavaFile().getFreeSpace() == 0 && at.parent() != null)
        {
            at = at.parent();
        }
        root = at;
    }

    @Override
    public Bytes free()
    {
        return Bytes.bytes(root.asJavaFile().getFreeSpace());
    }

    @Override
    public LocalFolder root()
    {
        return root;
    }

    @Override
    public Bytes size()
    {
        return Bytes.bytes(root.asJavaFile().getTotalSpace());
    }

    @Override
    public String toString()
    {
        return root.toString();
    }

    @Override
    public Bytes usable()
    {
        return Bytes.bytes(root.asJavaFile().getUsableSpace());
    }
}
