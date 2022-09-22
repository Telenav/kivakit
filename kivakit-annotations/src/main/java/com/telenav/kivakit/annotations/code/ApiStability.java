package com.telenav.kivakit.annotations.code;

/**
 * The level of API stability for a class, as evaluated by a developer. This is different from a measure of past source
 * code change because it is future-looking. It is based on the <i>anticipated</i> level of change in the future.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public enum ApiStability
{
    /** The API is not expected to change */
    STABLE,

    /** The API is not expected to change, except that new default methods may be added */
    STABLE_DEFAULT_EXPANDABLE,

    /** The API may be changed */
    UNSTABLE,

    /** The API has not been evaluated for stability */
    UNEVALUATED
}
