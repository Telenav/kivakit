package com.telenav.kivakit.annotations.code.quality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * The intended audience for a class or interface. Types annotated with {@link CodeQuality} default to an audience of
 * AUDIENCE_PUBLIC.
 *
 * @author jonathanl (shibo)
 * @see CodeQuality
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public enum Audience
{
    /** The annotated code is public and intended for end-users */
    AUDIENCE_PUBLIC,

    /** The annotated code is not intended for end-users, but is public to service provider implementers */
    AUDIENCE_SERVICE_PROVIDER_INTERFACE,

    /** The annotated code fulfills a service provider interface (SPI) */
    AUDIENCE_SERVICE_PROVIDER,

    /** The annotated code is private and should not be used outside the framework */
    AUDIENCE_INTERNAL;

    /**
     * Returns true if this value is AUDIENCE_INTERNAL
     */
    public boolean isPrivate()
    {
        return this == AUDIENCE_INTERNAL;
    }

    /**
     * Returns the opposite of {@link #isPrivate()}
     */
    public boolean isPublic()
    {
        return !isPrivate();
    }
}
