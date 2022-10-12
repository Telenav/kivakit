package com.telenav.kivakit.annotations.code.change;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Indicates a (potentially incompatible) code change to a method.
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * class MyClass
 * {
 *    {@literal @}MethodChange(change = METHOD_ADDED,
 *                            version = "1.8.0")
 *     default myNewMethod()
 *     {
 *         [...]
 *     }
 *
 *    {@literal @}MethodChange(change = METHOD_RENAMED,
 *                            previously = "void myOldMethod()"
 *                            version = "1.8.0")
 *     default myRenamedMethod()
 *     {
 *         [...]
 *     }
 * }</pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@CodeQuality(stability = STABILITY_UNDETERMINED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public @interface MethodChange
{
    /**
     * The type of change that occurred
     */
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    enum Change
    {
        /** The annotated method was added */
        METHOD_ADDED,

        /** The annotated method was renamed from the name it has {@link #previously()} */
        METHOD_RENAMED,

        /** The annotated method changed signature from what it was {@link #previously()} */
        METHOD_CHANGED_SIGNATURE
    }

    /**
     * Returns the change
     */
    Change change();

    /**
     * Returns any previous signature of the method, if it was renamed or its signature changed
     */
    String previously() default "";

    /**
     * Returns the version when the change occurred
     */
    String version();
}
