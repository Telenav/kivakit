package com.telenav.kivakit.annotations.code.quality;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_UNDETERMINED;

/**
 * Indicates the code stability, documentation completeness, code review process, and testing sufficiency of a class or
 * interface. The <a href="https://www.lexkai.org/">Lexakai</a> documentation tool uses this annotation to add code
 * quality metrics to automatically updated <i>README.md</i> markup files.
 *
 * <p><b>Example</b></p>
 *
 * <pre>{@literal @}CodeQuality(audience = AUDIENCE_PUBLIC,
 *             documentation = DOCUMENTATION_COMPLETE,
 *             stability = STABILITY_STABLE,
 *             testing = TESTING_SUFFICIENT,
 *             reviews = 1,
 *             reviewers = "shibo")
 * class MyClass
 * {
 *     [...]
 * }</pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
@CodeQuality(stability = STABILITY_UNDETERMINED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public @interface CodeQuality
{
    /**
     * Returns the audience for the annotated code
     */
    Audience audience() default AUDIENCE_PUBLIC;

    /**
     * Returns the subjective documentation quality, as evaluated by a developer
     */
    Documentation documentation() default DOCUMENTATION_UNDETERMINED;

    /**
     * Returns a list of reviewers
     */
    String[] reviewers() default "";

    /**
     * Returns the number of reviews of this class or interface
     */
    int reviews() default 0;

    /**
     * Returns the subjective likelihood of future code stability, as evaluated by a developer
     */
    Stability stability() default STABILITY_UNDETERMINED;

    /**
     * Returns the level of testing provided based versus the level needed, as evaluated by a developer
     */
    Testing testing() default TESTING_UNDETERMINED;
}
