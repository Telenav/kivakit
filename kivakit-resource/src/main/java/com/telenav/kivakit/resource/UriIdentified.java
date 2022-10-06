package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.CodeQuality;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Interface to an object with a {@link URI}
 */
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface UriIdentified
{
    /**
     * Returns the Universal Resource Identifier (URI) for this object
     */
    URI uri();
}
