package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.ApiQuality;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Interface to something that can be deleted
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
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
