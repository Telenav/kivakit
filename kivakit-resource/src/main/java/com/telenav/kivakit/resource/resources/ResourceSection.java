////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.resource.resources;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * A portion of a resource from one offset into the resource to another.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTING_NONE)
public class ResourceSection extends BaseReadableResource
{
    /** The resource to read */
    private final Resource resource;

    /** The start offset in the resource of the section to read */
    private final long startOffset;

    /** The end offset in the resource of the section to read */
    private final long endOffset;

    /**
     * @param resource The parent resource to read from
     * @param startOffset The start offset, inclusive
     * @param endOffset The end offset, exclusive
     */
    public ResourceSection(@NotNull Resource resource,
                           long startOffset,
                           long endOffset)
    {
        super(resource.path());
        this.resource = ensureNotNull(resource);
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        if (startOffset > endOffset)
        {
            throw new IllegalArgumentException("Start index of " + startOffset + " must be less than end index of " + endOffset);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time createdAt()
    {
        return resource().createdAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        InputStream in = resource.openForReading();
        IO.skip(this, in, startOffset);
        return new InputStream()
        {
            private long offset = startOffset;

            @Override
            public void close()
            {
                IO.close(ResourceSection.this, in);
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
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes sizeInBytes()
    {
        return Bytes.bytes(endOffset - startOffset);
    }

    @Override
    public String toString()
    {
        return "[ResourceSection resource = " + resource + ", start = " + startOffset + ", end = "
                + endOffset + "]";
    }
}
