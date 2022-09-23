package com.telenav.kivakit.annotations.code;

/**
 * An evaluation of the quality of documentation for this class, as determined by a developer. Some classes need more
 * documentation, some less, so subjective opinion is necessary to determine documentation quality level.
 *
 * @author jonathanl (shibo)
 */
public enum DocumentationQuality
{
    /** Documentation is complete */
    DOCUMENTED,

    /** Some documentation is available, but more is required */
    MORE_DOCUMENTATION_REQUIRED,

    /** No documentation is available */
    UNDOCUMENTED,

    /** Documentation status has not been evaluated */
    UNEVALUATED
}
