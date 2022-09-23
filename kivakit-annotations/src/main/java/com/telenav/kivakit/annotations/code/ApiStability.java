package com.telenav.kivakit.annotations.code;

/**
 * The level of API stability for a class, as evaluated by a developer. This is different from a measure of past source
 * code change because it is future-looking. It is based on the <i>anticipated</i> level of <i>incompatible</i> change
 * in the future.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public enum ApiStability
{
    STABLE,
    UNSTABLE,
    UNEVALUATED
}
