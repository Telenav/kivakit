package com.telenav.kivakit.resource;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Interface to something that can be deleted
 */
@SuppressWarnings("unused")
public interface Renamable
{
    /**
     * Returns true if the object was deleted, false otherwise
     */
    default boolean renameTo(ResourcePathed path)
    {
        return unsupported();
    }
}
