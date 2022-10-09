package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.writing.WritableResource;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Reads {@link SerializableObject}s from {@link Resource}s and writes them to {@link WritableResource}s.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = TESTING_NONE)
public interface ObjectSerializer extends
        ObjectReader,
        ObjectWriter
{
}
