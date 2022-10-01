package com.telenav.kivakit.annotations.code;

import static com.telenav.kivakit.annotations.code.ApiStability.MORE_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The level of API stability for a class, as evaluated by a developer. This is different from a measure of past source
 * code change because it is future-looking. It is based on the <i>anticipated</i> level of <i>incompatible</i> change
 * in the future.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = MORE_EVALUATION_NEEDED,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED,
            reviews = 1,
            reviewers = "shibo")
public enum ApiType
{
    /** The API is fully public and intended for end-users */
    PUBLIC_API,

    /** The API is not intended for end-users, but is public to service provider implementers */
    SERVICE_PROVIDER_API,

    /** The API is private and should not be used outside the framework */
    PRIVATE_API
}
