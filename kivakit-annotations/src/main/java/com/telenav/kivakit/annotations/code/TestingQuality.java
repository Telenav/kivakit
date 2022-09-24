package com.telenav.kivakit.annotations.code;

import static com.telenav.kivakit.annotations.code.ApiStability.MORE_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * The quality of tests for a given class, as evaluated by a developer. This is different from a simple code coverage
 * metric because there are many methods and even whole classes that do not need full testing.
 */
@ApiQuality(stability = MORE_EVALUATION_NEEDED,
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED,
            reviews = 1,
            reviewers = "shibo")
public enum TestingQuality
{
    /** All needed tests are implemented */
    FULLY_TESTED,

    /** No tests are needed */
    TESTING_NOT_REQUIRED,

    /** Some tests are implemented, but more are required */
    MORE_TESTING_REQUIRED,

    /** No tests have been implemented */
    UNTESTED,

    /** More evaluation of testing status is needed */
    MORE_EVALUATION_NEEDED,

    /** Testing status has not been evaluated */
    UNEVALUATED
}
