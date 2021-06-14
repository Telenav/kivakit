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

package com.telenav.kivakit.resource.resources.streamed;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An {@link OutputStream} stream wrapper that turns the output stream into a {@link WritableResource}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class OutputResource extends BaseWritableResource
{
    private final OutputStream out;

    public OutputResource(final OutputStream out)
    {
        this.out = out;
    }

    @Override
    public Boolean isWritable()
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

    @Override
    public Bytes sizeInBytes()
    {
        // Unknown size
        return null;
    }
}
