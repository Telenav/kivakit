////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;

public class Cache
{
    private final Folder root;

    public Cache(final Folder root)
    {
        this.root = root;
        if (!this.root.mkdirs().exists())
        {
            throw new IllegalStateException("Unable to create root folder for cache: " + this.root);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public File add(final Resource resource, final CopyMode mode, final ProgressReporter reporter)
    {
        final var file = file(resource);
        if (!file.exists())
        {
            resource.safeCopyTo(file, mode, reporter);
        }
        return file;
    }

    public File file(final FileName name)
    {
        return root.file(name);
    }

    public File file(final Resource resource)
    {
        return file(resource.fileName());
    }

    public Folder folder(final String name)
    {
        return root.folder(name);
    }
}
