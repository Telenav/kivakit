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

package com.telenav.kivakit.resource.resources.other;

import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;

/**
 * A portion of a resource from one offset into the resource to another.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
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
    public Bytes sizeInBytes()
    {
        return Bytes.bytes(endOffset - startOffset);
    }

    @Override
    public String toString()
    {
        return "[ResourceSection parent = " + parent + ", start = " + startOffset + ", end = "
                + endOffset + "]";
    }
}
