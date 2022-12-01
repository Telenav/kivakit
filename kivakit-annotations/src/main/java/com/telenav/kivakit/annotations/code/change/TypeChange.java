package com.telenav.kivakit.annotations.code.change;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

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
@Retention(SOURCE)
@Target(TYPE)
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
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    enum Change
    {
        /** The annotated type was added */
        TYPE_ADDED,

        /** The annotated type was renamed from the name it had {@link #previously()} */
        TYPE_RENAMED,

        /** The annotated type was moved from the location it was in {@link #previously()} */
        TYPE_MOVED,

        /** The method that existed {@link #previously()} was removed */
        METHOD_REMOVED
    }

    /**
     * Returns the change
     */
    Change change();

    /**
     * Returns the type that was renamed or moved, or the signature of the method that was removed
     */
    String previously() default "";

    /**
     * Returns the version when the change occurred
     */
    String version();
}
