////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.other;

import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.writing.BaseWritableResource;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A writable resource that accepts anything you write and destroys it, so when you read there is nothing to be seen.
 *
 * @author matthieun
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
public class NullResource extends BaseWritableResource
{
    public NullResource()
    {
        super(ResourcePath.parseUnixResourcePath("/objects/NullResource"));
    }

    @Override
    public Bytes bytes()
    {
        return null;
    }

    @Override
    public boolean isWritable()
    {
        // A null resource accepts it all.
        return true;
    }

    @Override
    public InputStream onOpenForReading()
    {
        return new InputStream()
        {
            @Override
            public int read()
            {
                return -1;
            }
        };
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return new OutputStream()
        {
            @Override
            public void write(final int b)
            {
            }
        };
    }
}
