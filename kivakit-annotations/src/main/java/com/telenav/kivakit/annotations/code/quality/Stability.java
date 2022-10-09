package com.telenav.kivakit.annotations.code.quality;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * The level of stability for a class or interface, as evaluated by a developer. This is different from a measure of
 * past source code change because it is future-looking. It is based on the <i>anticipated</i> level of
 * <i>incompatible</i> change in the future.
 *
 * @author jonathanl (shibo)
 * @see CodeQuality
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_UNDETERMINED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_PUBLIC,
             reviews = 1,
             reviewers = "shibo")
public enum Stability
{
    /** The code is not expected to change */
    STABILITY_STABLE,

    /** The code is not expected to change, except that new methods may be added */
    STABILITY_STABLE_EXTENSIBLE,

    /** The code may be changed */
    STABILITY_UNSTABLE,

    /** Requires more evaluation */
    STABILITY_UNDETERMINED;

    public boolean isStable()
    {
        return this == STABILITY_STABLE || this == STABILITY_STABLE_EXTENSIBLE;
    }

    public boolean isUnstable()
    {
        return this == STABILITY_UNSTABLE;
    }
}
