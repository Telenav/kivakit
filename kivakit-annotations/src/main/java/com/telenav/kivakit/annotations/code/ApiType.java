package com.telenav.kivakit.annotations.code;

import static com.telenav.kivakit.annotations.code.ApiStability.API_FURTHER_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The level of API stability for a class, as evaluated by a developer. This is different from a measure of past source
 * code change because it is future-looking. It is based on the <i>anticipated</i> level of <i>incompatible</i> change
 * in the future.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = API_FURTHER_EVALUATION_NEEDED,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE,
            reviews = 1,
            reviewers = "shibo")
public enum ApiType
{
    /** The API is fully public and intended for end-users */
    API_PUBLIC,

    /** The API is not intended for end-users, but is public to service provider implementers */
    API_SERVICE_PROVIDER,

    /** The API is private and should not be used outside the framework */
    API_PRIVATE
}
