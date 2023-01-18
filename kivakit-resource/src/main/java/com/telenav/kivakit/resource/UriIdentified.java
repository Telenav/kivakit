package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to an object with a {@link URI}
 */
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface UriIdentified
{
    /**
     * Returns the Universal Resource Identifier (URI) for this object
     */
    URI asUri();
}
