package com.telenav.kivakit.annotations.code.quality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * An evaluation of the quality of documentation for this class, as determined by a developer. Some classes need more
 * documentation, some less, so subjective opinion is necessary to determine documentation quality level.
 *
 * @author jonathanl (shibo)
 * @see TypeQuality
 */
@SuppressWarnings("unused")
@TypeQuality
    (
        stability = STABILITY_UNDETERMINED,
        testing = TESTING_NOT_NEEDED,
        documentation = DOCUMENTED,
        reviews = 1,
        reviewers = "shibo"
    )
public enum Documentation
{
    /** Documentation is complete */
    DOCUMENTED,

    /** Some documentation is available, but more is required */
    DOCUMENTATION_INSUFFICIENT,

    /** No documentation is available */
    UNDOCUMENTED,

    /** Documentation status has not been evaluated */
    DOCUMENTATION_UNDETERMINED,

    /** The method is trivial and needs no documentation */
    DOCUMENTATION_NOT_NEEDED;

    /**
     * Returns true if this value is DOCUMENTATION_COMPLETE
     */
    public boolean isComplete()
    {
        return this == DOCUMENTED;
    }

    /**
     * Returns the opposite of {@link #isComplete()}
     */
    public boolean isIncomplete()
    {
        return !isComplete();
    }
}
