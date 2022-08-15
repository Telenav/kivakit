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

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.reading.ReadableResource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

/**
 * An {@link InputStream} stream wrapper that allows *one-time* reading of an input stream as a {@link
 * ReadableResource}. Once the StreamResource has been opened, it cannot be opened a second time. Attempting to do so
 * will result in an {@link IllegalStateException}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class InputResource extends BaseReadableResource
{
    private final InputStream in;

    private boolean opened;

    /**
     * @param in The input stream (which can only be read one time)
     */
    public InputResource(InputStream in)
    {
        super(ResourcePath.parseUnixResourcePath(Listener.consoleListener(), "/objects/InputResource/" + Integer.toHexString(in.hashCode())));
        this.in = in;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        if (opened)
        {
            return fatal("StreamResource can only be read once.");
        }
        opened = true;
        return in;
    }

    @Override
    public Bytes sizeInBytes()
    {
        // Unknown size
        return null;
    }
}
