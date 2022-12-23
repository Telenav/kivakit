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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * An {@link OutputStream} stream wrapper that turns the output stream into a {@link WritableResource}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = UNTESTED)
public class OutputResource extends BaseWritableResource implements AutoCloseable
{
    /** The output stream to write to */
    private final OutputStream out;

    /** True if this resource has been opened */
    private boolean opened;

    /**
     * @param out The output stream to write to
     */
    public OutputResource(@NotNull OutputStream out)
    {
        this.out = out;
    }

    @Override
    public void close() throws Exception
    {
        out.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isWritable()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream onOpenForWriting(WriteMode mode)
    {
        mode.ensureAllowed(out);
        if (opened)
        {
            return fatal("OutputResource can only be written to once.");
        }
        opened = true;
        return out;
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
