package com.telenav.kivakit.annotations.code.quality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * The quality of tests for a given class, as evaluated by a developer. This is different from a simple code coverage
 * metric because there are many methods and even whole classes that do not need full testing. Some don't need testing
 * at all, for example, most interfaces.
 *
 * @author jonathanl (shibo)
 * @see CodeQuality
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_UNDETERMINED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public enum Testing
{
    /** All needed tests are implemented */
    TESTED,

    /** No tests are needed */
    TESTING_NOT_NEEDED,

    /** Some tests are implemented, but more are required */
    TESTING_INSUFFICIENT,

    /** No tests have been implemented */
    UNTESTED,

    /** Testing status has not been evaluated */
    TESTING_UNDETERMINED;

    /**
     * Returns true if testing is either sufficient, or not needed
     */
    public boolean isTested()
    {
        return this == TESTED || this == TESTING_NOT_NEEDED;
    }

    /**
     * Returns the opposite of {@link #isTested()}
     */
    public boolean isUntested()
    {
        return !isTested();
    }
}
