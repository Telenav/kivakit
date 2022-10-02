package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.writing.WritableResource;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Reads {@link SerializableObject}s from {@link Resource}s and writes them to {@link WritableResource}s.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            documentation = FULLY_DOCUMENTED,
            testing = UNTESTED)
public interface ObjectSerializer extends
        ObjectReader,
        ObjectWriter
{
}
