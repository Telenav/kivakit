package com.telenav.kivakit.annotations.code.quality;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_UNDETERMINED;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates the intended audience, subjective stability, documentation completeness, testing sufficiency and code
 * review process of a type (class, record or interface). The <a href="https://www.lexkai.org/">Lexakai</a>
 * documentation tool uses this annotation to add code quality metrics to automatically updated <i>README.md</i> markup
 * files.
 *
 * <p><b>Example</b></p>
 *
 * <pre>{@literal @}TypeQuality
 * (
 *     audience = AUDIENCE_INTERNAL,
 *     documentation = DOCUMENTATION_COMPLETE,
 *     stability = STABLE,
 *     reviews = 1,
 *     reviewers = "shibo"
 * )
 * class MyClass
 * {
 *     [...]
 * }</pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "JavadocDeclaration" })
@Retention(SOURCE)
@Target(TYPE)
@TypeQuality
    (
        audience = AUDIENCE_PUBLIC,
        stability = STABLE,
        documentation = DOCUMENTED
    )
public @interface TypeQuality
{
    /**
     * Returns the audience for the annotated code. By default, all types are AUDIENCE_PUBLIC.
     */
    Audience audience() default AUDIENCE_PUBLIC;

    /**
     * Returns the subjective documentation quality, as evaluated by a developer. Until explicitly evaluated and
     * annotated, a type's {@link #documentation()} defaults to DOCUMENTATION_UNDETERMINED.
     */
    Documentation documentation() default DOCUMENTATION_UNDETERMINED;

    /**
     * Returns an array of reviewers, denoted by name, email or username.
     */
    String[] reviewers() default {};

    /**
     * Returns the number of reviews of this class or interface. The default is zero.
     */
    int reviews() default 0;

    /**
     * Returns the subjective likelihood of future code stability, as evaluated by a developer. Until explicitly
     * evaluated and annotated, a type's {@link #stability()} defaults to STABILITY_UNDETERMINED.
     */
    Stability stability() default STABILITY_UNDETERMINED;

    /**
     * Returns the level of testing provided based versus the level needed, as evaluated by a developer. Until
     * explicitly evaluated and annotated, a type's {@link #testing()} defaults to TESTING_UNDETERMINED.
     */
    Testing testing() default TESTING_UNDETERMINED;
}
