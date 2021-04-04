////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.streamed;

import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.reading.BaseReadableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * A stream resource is a limited wrapper around an input stream that allows one-time reading of an input stream as a
 * Resource. Once the StreamResource has been opened, it cannot be opened a second time. Attempting to do so will result
 * in an {@link IllegalStateException}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
public class InputResource extends BaseReadableResource
{
    private final InputStream in;

    private boolean opened;

    /**
     * @param in The input stream (which can only be read one time)
     */
    public InputResource(final InputStream in)
    {
        super(ResourcePath.parseUnixResourcePath("/objects/InputResource/" + Integer.toHexString(in.hashCode())));
        this.in = in;
    }

    @Override
    public Bytes bytes()
    {
        // Unknown size
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        if (opened)
        {
            return fail("StreamResource can only be read once.");
        }
        opened = true;
        return in;
    }
}
