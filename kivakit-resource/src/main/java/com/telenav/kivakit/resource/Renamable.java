package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.ApiQuality;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Interface to something that can be deleted
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE)
public interface Renamable
{
    /**
     * Returns true if the object was deleted, false otherwise
     */
    default boolean renameTo(@NotNull ResourcePathed path)
    {
        return unsupported();
    }
}
