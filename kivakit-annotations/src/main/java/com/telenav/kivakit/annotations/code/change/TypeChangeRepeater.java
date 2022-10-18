package com.telenav.kivakit.annotations.code.change;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <b>Not public API</b>
 *
 * <p>
 * This annotation is used to allow the {@link TypeChange} annotation to be repeated.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@Retention(RUNTIME)
@Target(TYPE)
@CodeQuality(audience = AUDIENCE_INTERNAL,
             stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public @interface TypeChangeRepeater
{
    TypeChange[] value();
}
