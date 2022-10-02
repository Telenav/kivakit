package com.telenav.kivakit.annotations.code;

import static com.telenav.kivakit.annotations.code.ApiStability.API_FURTHER_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The quality of tests for a given class, as evaluated by a developer. This is different from a simple code coverage
 * metric because there are many methods and even whole classes that do not need full testing.
 */
@ApiQuality(stability = API_FURTHER_EVALUATION_NEEDED,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE,
            reviews = 1,
            reviewers = "shibo")
public enum TestingQuality
{
    /** All needed tests are implemented */
    TESTING_SUFFICIENT,

    /** No tests are needed */
    TESTING_NOT_NEEDED,

    /** Some tests are implemented, but more are required */
    TESTING_INSUFFICIENT,

    /** No tests have been implemented */
    TESTING_NONE,

    /** More evaluation of testing status is needed */
    TESTING_EVALUATION_NEEDED,

    /** Testing status has not been evaluated */
    TESTING_UNEVALUATED
}
