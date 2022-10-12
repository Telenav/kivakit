package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;

/**
 * Object metadata to be included with serialized objects.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
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
        return arrayContains(metadata, this);
    }
}
