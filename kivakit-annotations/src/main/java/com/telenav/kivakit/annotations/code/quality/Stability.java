package com.telenav.kivakit.annotations.code.quality;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;

/**
 * The level of stability for a class or interface, as evaluated by a developer. This is different from a measure of
 * past source code change because it is future-looking. It is based on the <i>anticipated</i> level of
 * <i>incompatible</i> change in the future.
 *
 * @author jonathanl (shibo)
 * @see TypeQuality
 */
@SuppressWarnings("unused")
@TypeQuality
    (
        stability = STABILITY_UNDETERMINED,
        documentation = DOCUMENTED,
        audience = AUDIENCE_PUBLIC,
        reviews = 1,
        reviewers = "shibo"
    )
public enum Stability
{
    /** The annotated type is not expected to change */
    STABLE,

    /**
     * The type is not expected to change, except that new methods may be added. Adding new methods to a type can
     * sometimes cause problems with compatibility if a method with the same name already exists in a subtype.
     */
    STABLE_EXTENSIBLE,

    /**
     * It is expected that this type will change incompatibly in the future. Using this type may require some
     * refactoring or other code adaptation in the future.
     */
    UNSTABLE,

    /** The stability of this type requires more evaluation */
    STABILITY_UNDETERMINED;

    /**
     * Returns true if this value is STABLE or STABLE_EXTENSIBLE.
     */
    public boolean isStable()
    {
        return this == STABLE || this == STABLE_EXTENSIBLE;
    }

    /**
     * Returns the opposite of {@link #isStable()}
     */
    public boolean isUnstable()
    {
        return !isStable();
    }
}
