package com.telenav.kivakit.annotations.code.quality;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_UNDETERMINED;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates the intended audience, documentation completeness and testing sufficiency of a method. The <a
 * href="https://www.lexkai.org/">Lexakai</a> documentation tool uses this annotation to add code quality metrics to
 * automatically updated <i>README.md</i> markup files.
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * class MyClass
 * {
 *    {@literal @}MethodQuality(audience = AUDIENCE_INTERNAL,
 *     documentation = DOCUMENTATION_COMPLETE,
 *     testing = TESTED)
 *     void myMethod()
 *     {
 *         [...]
 *     }
 * }</pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "JavadocDeclaration" })
@Retention(SOURCE)
@Target({METHOD, CONSTRUCTOR})
@TypeQuality
    (
        audience = AUDIENCE_PUBLIC,
        documentation = DOCUMENTED,
        stability = STABLE
    )
public @interface MethodQuality
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
     * Returns the level of testing provided based versus the level needed, as evaluated by a developer. Until
     * explicitly evaluated and annotated, a type's {@link #testing()} defaults to TESTING_UNDETERMINED.
     */
    Testing testing() default TESTING_UNDETERMINED;
}
