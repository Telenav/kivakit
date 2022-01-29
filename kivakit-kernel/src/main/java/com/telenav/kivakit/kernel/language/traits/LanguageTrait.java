package com.telenav.kivakit.kernel.language.traits;

import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;

import java.util.function.Function;

/**
 * Contains methods that substitute-for, or extend Java language features.
 *
 * <p><b>Functions</b></p>
 *
 * <ul>
 *     <li>applyIfNonNull - Null-safe version of Function.apply()</li>
 *     <li>nonNullOr - The given value if it is non-null, the default value otherwise.</li>
 * </ul>
 *
 * <p><b>Messaging</b></p>
 *
 * <ul>
 *     <li>isFalseOr - Broadcasts a {@link Problem} if the given value is not false</li>
 *     <li>isTrueOr - Broadcasts a {@link Problem} if the given value is not true</li>
 *     <li>isNonNullOr - Broadcasts a {@link Problem} if the given value is null</li>
 * </ul>
 *
 * <p><b>Function Examples</b></p>
 *
 * <pre>
 * applyIfNonNull(null, String::toLowerCase)   ==>   null
 * applyIfNonNull("JL", String::toLowerCase)   ==>   "jl"
 *
 * nonNullOr(5, -1)     ==>   5
 * nonNullOr(null, 5)   ==>   5</pre>
 *
 * <p><b>Messaging Examples</b></p>
 *
 * <pre>
 * String name = "bob";
 *
 * // This if-statement is the same as the following if/else statement.
 * if (isNonNullOr(name, "A name must be provided"))
 * {
 *     System.out.println("name = " + name);
 * }
 *
 * if (name != null)
 * {
 *     System.out.println("name = " + name);
 * }
 * else
 * {
 *     problem("A name must be provided")
 * }</pre>
 *
 * @author jonathanl (shibo)
 */
public interface LanguageTrait extends Broadcaster
{
    /**
     * <p>
     * If the given value is null, this method returns null. Otherwise, it returns the value of the given function when
     * it is applied to the given value.
     * </p>
     *
     * <pre>
     * var lowercase = applyIfNonNull(text, String::toLowerCase)
     * </pre>
     *
     * @param value The value to check for null
     * @param function The function to apply if the value is non-null
     */
    default <In, Out> Out applyIfNonNull(In value, Function<In, Out> function)
    {
        return value == null ? null : function.apply(value);
    }

    /**
     * If the value is true, broadcasts the given message as a {@link Problem}.
     *
     * @param value The value to test
     * @param message The problem message
     * @param arguments Any arguments to apply when formatting the {@link Problem} message.
     * @return True if the value is false, false if the value is true
     */
    default boolean isFalseOr(boolean value, String message, Object... arguments)
    {
        if (value)
        {
            problem(message, arguments);
        }

        return !value;
    }

    /**
     * If the value is null, broadcasts the given message as a {@link Problem}
     *
     * @param value The value to test
     * @param message The message to broadcast if the value is null
     * @param arguments Any arguments to apply when formatting the {@link Problem} message.
     * @return True if the value is non-null, false if the value is null
     */
    default boolean isNonNullOr(Object value, String message, Object... arguments)
    {
        if (value == null)
        {
            problem(message, arguments);
        }

        return value != null;
    }

    /**
     * If the value is false, broadcasts the given message as a {@link Problem}.
     *
     * @param value The value to test
     * @param message The problem message
     * @param arguments Any arguments to apply when formatting the {@link Problem} message.
     * @return True if the value is true
     */
    default boolean isTrueOr(boolean value, String message, Object... arguments)
    {
        if (!value)
        {
            problem(message, arguments);
        }
        return value;
    }

    /**
     * Returns the given value, but if the value is null, returns the defaultValue instead.
     *
     * @param value The provided value
     * @param defaultValue The default value to return if the value provided is null
     * @return The given value if it is non-null, the default value otherwise.
     */
    default <T> T nonNullOr(T value, T defaultValue)
    {
        return Objects.nonNullOr(value, defaultValue);
    }
}
