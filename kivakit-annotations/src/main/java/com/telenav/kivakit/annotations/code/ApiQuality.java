package com.telenav.kivakit.annotations.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.ApiStability.MORE_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.ApiStability.UNEVALUATED;
import static com.telenav.kivakit.annotations.code.ApiType.PUBLIC_API;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Indicates the API stability, documentation completeness, code reviews, and testing sufficiency of a class or
 * interface.
 *
 * <p><b>Example</b></p>
 *
 * <pre>{@literal @}ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
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
@ApiQuality(stability = MORE_EVALUATION_NEEDED,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED,
            reviews = 1,
            reviewers = "shibo")
public @interface ApiQuality
{
    /**
     * Returns the subjective documentation quality, as evaluated by a developer
     */
    DocumentationQuality[] documentation() default DocumentationQuality.UNEVALUATED;

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
    ApiStability[] stability() default UNEVALUATED;

    /**
     * Returns the level of testing provided based versus the level needed, as evaluated by a developer
     */
    TestingQuality[] testing() default TestingQuality.UNEVALUATED;

    /**
     * Returns the type of API
     */
    ApiType type() default PUBLIC_API;
}
