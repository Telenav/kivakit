package com.telenav.kivakit.core.language.trait;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.function.Functions;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.messages.status.Problem;

import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;

/**
 * <p>Adds methods that help in general to extend KivaKit's expressive capabilities in the Java language</p>
 *
 * <p><b>Functions</b></p>
 *
 * <ul>
 *     <li>{@link #ifNonNullApply(Object, Function)} - Null-safe version of Function.apply()</li>
 *     <li>{@link #ifNullDefault(Object, Object)} - The given value if it is non-null, the default value otherwise.</li>
 * </ul>
 *
 * <p><b>Validation</b></p>
 *
 * <ul>
 *     <li>{@link #isFalseOr(boolean, String, Object...)} - Broadcasts a {@link Problem} if the given value is not false</li>
 *     <li>{@link #isTrueOr(boolean, String, Object...)} - Broadcasts a {@link Problem} if the given value is not true</li>
 *     <li>{@link #isNonNullOr(Object, String, Object...)} - Broadcasts a {@link Problem} if the given value is null</li>
 * </ul>
 *
 * <p><b>Function Examples</b></p>
 *
 * <pre>
 * ifNonNullApply(null, String::toLowerCase)   ==>   null
 * ifNonNullApply("JL", String::toLowerCase)   ==>   "jl"
 *
 * ifNullDefault(5, -1)     ==>   5
 * ifNullDefault(null, 5)   ==>   5</pre>
 *
 * <p><b>Validation Examples</b></p>
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
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_DEFAULT_EXPANDABLE,
             documentation = SUFFICIENT,
             testing = NONE)
public interface LanguageTrait extends Repeater
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
    default <In, Out> Out ifNonNullApply(In value, Function<In, Out> function)
    {
        return Functions.apply(value, function);
    }

    /**
     * Returns the given value, but if the value is null, returns the defaultValue instead.
     *
     * @param value The provided value
     * @param defaultValue The default value to return if the value provided is null
     * @return The given value if it is non-null, the default value otherwise.
     */
    default <T> T ifNullDefault(T value, T defaultValue)
    {
        return Objects.nonNullOr(value, defaultValue);
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
}
