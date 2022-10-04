package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.writing.WritableResource;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Reads {@link SerializableObject}s from {@link Resource}s and writes them to {@link WritableResource}s.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTING_NONE)
public interface ObjectSerializer extends
        ObjectReader,
        ObjectWriter
{
}
