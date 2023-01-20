package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.writing.WritableResource;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Reads {@link SerializableObject}s from {@link Resource}s and writes them to {@link WritableResource}s.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public interface ObjectSerializer extends
        ObjectReader,
        ObjectWriter
{
}
