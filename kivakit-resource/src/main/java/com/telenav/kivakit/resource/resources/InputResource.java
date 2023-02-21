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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.kivakit.resource.reading.ReadableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.resource.ResourcePath.resourcePath;
import static java.lang.Integer.toHexString;

/**
 * An {@link InputStream} stream wrapper that allows *one-time* reading of an input stream as a
 * {@link ReadableResource}. Once the StreamResource has been opened, it cannot be opened a second time. Attempting to
 * do so will result in an {@link IllegalStateException}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public class InputResource extends BaseReadableResource implements AutoCloseable
{
    /** The input stream to read */
    private final InputStream in;

    /** True if this resource has been opened */
    private boolean opened;

    /**
     * @param in The input stream (which can only be read one time)
     */
    public InputResource(@NotNull InputStream in)
    {
        super(resourcePath("object://" + in.getClass().getSimpleName() + "/" + toHexString(in.hashCode())));

        this.in = in;
    }

    @Override
    public void close() throws Exception
    {
        in.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        if (opened)
        {
            return fatal("InputResource can only be read once.");
        }
        opened = true;
        return in;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes sizeInBytes()
    {
        // Unknown size
        return null;
    }
}
