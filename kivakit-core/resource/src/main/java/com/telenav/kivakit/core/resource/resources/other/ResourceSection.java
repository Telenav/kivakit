////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.other;

import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.reading.BaseReadableResource;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;

@UmlClassDiagram(diagram = DiagramResourceType.class)
public class ResourceSection extends BaseReadableResource
{
    private final Resource parent;

    private final long startOffset;

    private final long endOffset;

    /**
     * @param parent The parent resource to read from
     * @param startOffset The start offset, inclusive
     * @param endOffset The end offset, exclusive
     */
    public ResourceSection(final Resource parent, final long startOffset, final long endOffset)
    {
        super(parent.path());
        this.parent = parent;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        if (startOffset > endOffset)
        {
            throw new IllegalStateException(
                    "Start index of " + startOffset + " must be less than end index of " + endOffset);
        }
    }

    @Override
    public Bytes bytes()
    {
        return Bytes.bytes(endOffset - startOffset);
    }

    @Override
    public InputStream onOpenForReading()
    {
        return new InputStream()
        {
            private final InputStream in = parent.openForReading();

            private long offset = startOffset;

            @Override
            public void close()
            {
                IO.close(in);
            }

            @Override
            public int read() throws IOException
            {
                if (offset++ < endOffset)
                {
                    return in.read();
                }
                return -1;
            }

            {
                IO.skip(in, startOffset);
            }
        };
    }

    @Override
    public String toString()
    {
        return "[ResourceSection parent = " + parent + ", start = " + startOffset + ", end = "
                + endOffset + "]";
    }
}
