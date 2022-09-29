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
public enum ApiStability
{
    /** The API is not public, and should not be used outside of the KivaKit framework */
    NOT_PUBLIC,

    /** The API is not expected to change */
    STABLE,

    /** The API is not expected to change, except that new methods may be added */
    STABLE_EXTENSIBLE,

    /** The API is not expected to change, except that new static methods may be added */
    STABLE_STATIC_EXTENSIBLE,

    /** The API is not expected to change, except that new default methods may be added */
    STABLE_DEFAULT_EXTENSIBLE,

    /** The API is not expected to change, except that new enum values may be added */
    STABLE_ENUM_EXTENSIBLE,

    /** The API may be changed */
    UNSTABLE,

    /** Requires more evaluation */
    MORE_EVALUATION_NEEDED,

    /** The API has not been evaluated for stability */
    UNEVALUATED
}
