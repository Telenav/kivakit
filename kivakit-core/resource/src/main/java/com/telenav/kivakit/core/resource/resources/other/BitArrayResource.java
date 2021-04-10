////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.other;

import com.telenav.kivakit.core.collections.primitive.array.bits.BitArray;
import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitReader;
import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitWriter;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link WritableResource} that uses an underlying {@link BitArray} for its data.
 *
 * @author matthieun
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class BitArrayResource extends BaseWritableResource
{
    private final BitArray data;

    public BitArrayResource()
    {
        super(ResourcePath.parseUnixResourcePath("/objects/BitResource"));
        data = new BitArray("data");
    }

    @Override
    public Bytes bytes()
    {
        return Bytes.bytes(data.size());
    }

    @Override
    public boolean isWritable()
    {
        return true;
    }

    @Override
    public InputStream onOpenForReading()
    {
        return new InputStream()
        {
            private final BitReader reader = data.reader();

            @Override
            public int read()
            {
                return reader.read(8);
            }
        };
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return new OutputStream()
        {
            private final BitWriter writer = data.writer();

            @Override
            public void write(final int argument)
            {
                // Write only the 8 bits, as per the OutputStream contract
                writer.write(argument, 8);
            }
        };
    }
}
