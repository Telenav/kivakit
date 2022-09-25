package com.telenav.kivakit.annotations.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.ApiStability.*;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

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
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED,
            reviews = 1,
            reviewers = "shibo")
public @interface ApiQuality
{
    /**
     * @return The subjective documentation quality, as evaluated by a developer
     */
    DocumentationQuality[] documentation() default DocumentationQuality.UNEVALUATED;

    /**
     * @return A comma-separated list of reviewers
     */
    String[] reviewers() default "";

    /**
     * @return The number of reviews of this class or interface
     */
    int reviews() default 0;

    /**
     * @return The subjective likelihood of future API stability, as evaluated by a developer
     */
    ApiStability[] stability() default UNEVALUATED;

    /**
     * @return The level of testing provided based versus the level needed, as evaluated by a developer
     */
    TestingQuality[] testing() default TestingQuality.UNEVALUATED;
}
