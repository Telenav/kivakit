package com.telenav.kivakit.annotations.code;

/**
 * The quality of tests for a given class, as evaluated by a developer. This is different from a simple code coverage
 * metric because there are many methods and even whole classes that do not need full testing.
 */
public enum TestingQuality
{
    /** All needed tests are implemented */
    TESTED,

    /** No tests are needed */
    TESTING_NOT_REQUIRED,

    /** Some tests are implemented, but more are required */
    MORE_TESTING_REQUIRED,

    /** No tests have been implemented */
    NOT_TESTED,

    /** Testing status has not been evaluated */
    UNEVALUATED
}
