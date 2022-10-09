package com.telenav.kivakit.annotations.code.quality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * An evaluation of the quality of documentation for this class, as determined by a developer. Some classes need more
 * documentation, some less, so subjective opinion is necessary to determine documentation quality level.
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
public enum Documentation
{
    /** Documentation is complete */
    DOCUMENTATION_COMPLETE,

    /** Some documentation is available, but more is required */
    DOCUMENTATION_INSUFFICIENT,

    /** No documentation is available */
    DOCUMENTATION_NONE,

    /** Documentation status has not been evaluated */
    DOCUMENTATION_UNDETERMINED;

    public boolean isComplete()
    {
        return this == DOCUMENTATION_COMPLETE;
    }

    public boolean isIncomplete()
    {
        return !isComplete();
    }
}
