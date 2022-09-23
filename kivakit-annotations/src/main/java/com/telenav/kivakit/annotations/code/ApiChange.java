package com.telenav.kivakit.annotations.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates an incompatible API change.
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * {@literal @}ApiChange(change = REMOVED,
 *             method = "void myOldMethod(int value)",
 *             version = "1.8.0")
 * class MyClass
 * {
 *    {@literal @}ApiChange(change = ADDED, version = "1.8.0")
 *     default myNewMethod()
 *     {
 *         [...]
 *     }
 *
 * }</pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ApiChange
{
    /**
     * The type of change that occurred
     */
    enum MethodChangeType
    {
        ADDED,
        REMOVED,
        CHANGED_SIGNATURE,
        DEPRECATED
    }

    /**
     * @return The change
     */
    MethodChangeType change();

    /**
     * @return The changed method
     */
    String method() default "";

    /**
     * @return The API version when the change occurred
     */
    String version();
}
