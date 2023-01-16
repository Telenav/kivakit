package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;

/**
 * Object metadata to be included with serialized objects.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public enum ObjectMetadata
{
    /** The object class */
    METADATA_OBJECT_TYPE,

    /** The object version */
    METADATA_OBJECT_VERSION,

    /** Any instance identifier (in the case of serialized settings, for example) */
    METADATA_OBJECT_INSTANCE;

    public boolean containedIn(@NotNull ObjectMetadata[] metadata)
    {
        return arrayContains(metadata, this);
    }
}
