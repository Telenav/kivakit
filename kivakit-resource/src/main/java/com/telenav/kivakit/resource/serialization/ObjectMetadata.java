package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.core.language.Arrays;

/**
 * Object metadata to be included with serialized objects.
 *
 * @author jonathanl (shibo)
 */
public enum ObjectMetadata
{
    /** The object class */
    TYPE,

    /** The object version */
    VERSION,

    /** Any instance identifier (in the case of serialized settings, for example) */
    INSTANCE;

    public boolean containedIn(ObjectMetadata[] metadata)
    {
        return Arrays.contains(metadata, this);
    }
}
