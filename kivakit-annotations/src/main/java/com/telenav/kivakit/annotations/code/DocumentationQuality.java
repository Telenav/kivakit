package com.telenav.kivakit.annotations.code;

import static com.telenav.kivakit.annotations.code.ApiStability.MORE_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * An evaluation of the quality of documentation for this class, as determined by a developer. Some classes need more
 * documentation, some less, so subjective opinion is necessary to determine documentation quality level.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = MORE_EVALUATION_NEEDED,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED,
            reviews = 1,
            reviewers = "shibo")
public enum DocumentationQuality
{
    /** Documentation is complete */
    FULLY_DOCUMENTED,

    /** Some documentation is available, but more is required */
    MORE_DOCUMENTATION_NEEDED,

    /** No documentation is available */
    UNDOCUMENTED,

    /** Documentation status has not been evaluated */
    UNEVALUATED
}
