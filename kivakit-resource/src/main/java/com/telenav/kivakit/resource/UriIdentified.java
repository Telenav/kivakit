package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to an object with a {@link URI}
 */
@CodeQuality(stability = STABILITY_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface UriIdentified
{
    /**
     * Returns the Universal Resource Identifier (URI) for this object
     */
    URI uri();
}
