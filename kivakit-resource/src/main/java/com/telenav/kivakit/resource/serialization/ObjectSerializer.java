package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.writing.WritableResource;

/**
 * Reads {@link SerializableObject}s from {@link Resource}s and writes them to {@link WritableResource}s.
 *
 * @author jonathanl (shibo)
 */
public interface ObjectSerializer extends
        ObjectReader,
        ObjectWriter
{
}
