package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.Arrays;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_ENUM_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Object metadata to be included with serialized objects.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_ENUM_EXTENSIBLE,
            documentation = FULLY_DOCUMENTED,
            testing = UNTESTED)
public enum ObjectMetadata
{
    /** The object class */
    OBJECT_TYPE,

    /** The object version */
    OBJECT_VERSION,

    /** Any instance identifier (in the case of serialized settings, for example) */
    OBJECT_INSTANCE;

    public boolean containedIn(@NotNull ObjectMetadata[] metadata)
    {
        return Arrays.contains(metadata, this);
    }
}
