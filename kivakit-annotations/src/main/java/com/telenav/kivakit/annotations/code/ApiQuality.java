package com.telenav.kivakit.annotations.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the API stability, documentation completeness and testing sufficiency of a class.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
public @interface ApiQuality
{
    /**
     * @return The subjective documentation quality, as evaluated by a developer
     */
    DocumentationQuality documentation() default DocumentationQuality.UNEVALUATED;

    /**
     * @return The subjective likelihood of future API stability, as evaluated by a developer
     */
    ApiStability stability() default ApiStability.UNEVALUATED;

    /**
     * @return The level of testing provided based versus the level needed, as evaluated by a developer
     */
    TestingQuality testing() default TestingQuality.UNEVALUATED;
}
