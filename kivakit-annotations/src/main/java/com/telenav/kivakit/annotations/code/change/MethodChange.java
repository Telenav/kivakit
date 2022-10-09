package com.telenav.kivakit.annotations.code.change;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
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
 *                            previousSignature = "void myOldMethod()"
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
    enum Change
    {
        METHOD_ADDED,
        METHOD_RENAMED,
        METHOD_CHANGED_SIGNATURE
    }

    /**
     * @return The change
     */
    Change change();

    /**
     * @return Any previous signature of the method, if it was renamed or its signature changed
     */
    String previously() default "";

    /**
     * @return The API version when the change occurred
     */
    String version();
}
