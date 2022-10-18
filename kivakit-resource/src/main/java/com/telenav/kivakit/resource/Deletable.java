package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Interface to something that can be deleted
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
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
