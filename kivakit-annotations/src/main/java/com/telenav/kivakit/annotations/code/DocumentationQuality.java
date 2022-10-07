package com.telenav.kivakit.annotations.code;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_FURTHER_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * An evaluation of the quality of documentation for this class, as determined by a developer. Some classes need more
 * documentation, some less, so subjective opinion is necessary to determine documentation quality level.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_FURTHER_EVALUATION_NEEDED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public enum DocumentationQuality
{
    /** Documentation is complete */
    DOCUMENTATION_COMPLETE,

    /** Some documentation is available, but more is required */
    DOCUMENTATION_INSUFFICIENT,

    /** No documentation is available */
    DOCUMENTATION_NONE,

    /** Documentation status has not been evaluated */
    DOCUMENTATION_UNEVALUATED;

    public boolean isComplete()
    {
        return this == DOCUMENTATION_COMPLETE;
    }

    public boolean isIncomplete()
    {
        return !isComplete();
    }
}
