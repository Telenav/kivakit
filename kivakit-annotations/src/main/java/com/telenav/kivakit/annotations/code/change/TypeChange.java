package com.telenav.kivakit.annotations.code.change;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Indicates a (potentially incompatible) code change to a type.
 *
 * <p><b>Example</b></p>
 *
 * <pre> {@literal @}MethodChange(change = METHOD_REMOVED,
 *               previously = "void myOldMethod(int value)",
 *               version = "1.8.0")
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
public @interface TypeChange
{
    /**
     * The type of change that occurred
     */
    enum Change
    {
        TYPE_ADDED,
        TYPE_RENAMED,
        TYPE_MOVED,
        METHOD_REMOVED
    }

    /**
     * @return The change
     */
    Change change();

    /**
     * @return The type that was renamed or moved, or the signature of the method that was removed
     */
    String previously() default "";

    /**
     * @return The version when the change occurred
     */
    String version();
}
