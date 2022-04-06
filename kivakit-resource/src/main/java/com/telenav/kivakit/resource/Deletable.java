package com.telenav.kivakit.resource;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Interface to something that can be deleted
 */
public interface Deletable
{
    /**
     * Returns true if the object was deleted, false otherwise
     */
    default boolean delete()
    {
        return unsupported();
    }
}
