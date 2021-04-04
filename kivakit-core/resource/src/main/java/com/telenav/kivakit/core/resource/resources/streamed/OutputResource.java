////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.streamed;

import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;

@UmlClassDiagram(diagram = DiagramResourceType.class)
public class OutputResource extends BaseWritableResource
{
    private final OutputStream out;

    public OutputResource(final OutputStream out)
    {
        this.out = out;
    }

    @Override
    public Bytes bytes()
    {
        // Unknown size
        return null;
    }

    @Override
    public boolean isWritable()
    {
        return true;
    }

    @Override
    public InputStream onOpenForReading()
    {
        return null;
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return out;
    }
}
