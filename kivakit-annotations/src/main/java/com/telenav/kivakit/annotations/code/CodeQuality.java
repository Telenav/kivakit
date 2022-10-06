package com.telenav.kivakit.annotations.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_FURTHER_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.CodeStability.CODE_UNEVALUATED;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_PUBLIC;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_UNEVALUATED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_UNEVALUATED;

/**
 * Indicates the API stability, documentation completeness, code reviews, and testing sufficiency of a class or
 * interface.
 *
 * <p><b>Example</b></p>
 *
 * <pre>{@literal @}CodeQuality(stability = STABLE_DEFAULT_EXPANDABLE,
 *            testing = FULLY_TESTED,
 *            documentation = FULLY_DOCUMENTED,
 *            reviews = 1,
 *            reviewers = "shibo")
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
@CodeQuality(stability = CODE_FURTHER_EVALUATION_NEEDED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public @interface CodeQuality
{
    /**
     * Returns the subjective documentation quality, as evaluated by a developer
     */
    DocumentationQuality documentation() default DOCUMENTATION_UNEVALUATED;

    /**
     * Returns a comma-separated list of reviewers
     */
    String[] reviewers() default "";

    /**
     * Returns the number of reviews of this class or interface
     */
    int reviews() default 0;

    /**
     * Returns the subjective likelihood of future API stability, as evaluated by a developer
     */
    CodeStability[] stability() default CODE_UNEVALUATED;

    /**
     * Returns the level of testing provided based versus the level needed, as evaluated by a developer
     */
    TestingQuality[] testing() default TESTING_UNEVALUATED;

    /**
     * Returns the type of API
     */
    CodeType type() default CODE_PUBLIC;
}
