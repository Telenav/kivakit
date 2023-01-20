package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Interface to something that can be deleted
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
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
