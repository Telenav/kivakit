package com.telenav.kivakit.kernel.language.traits;

import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.language.functions.Functions;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;

import java.util.function.Function;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Contains methods that substitute-for, or extend Java language features.
 *
 * <p><b>Functions</b></p>
 *
 * <ul>
 *     <li>ifNonNullApply - Null-safe version of Function.apply()</li>
 *     <li>ifNullDefault - The given value if it is non-null, the default value otherwise.</li>
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
 * ifNonNullApply(null, String::toLowerCase)   ==>   null
 * ifNonNullApply("JL", String::toLowerCase)   ==>   "jl"
 *
 * ifNullDefault(5, -1)     ==>   5
 * ifNullDefault(null, 5)   ==>   5</pre>
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
public interface LanguageTrait extends TryTrait, Repeater
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

    /**
     * If the given {@link Validatable} object is valid, returns true. Otherwise, broadcasts the given message as a
     * Problem and returns false.
     *
     * @param validatable The object to validate
     * @param message The message to broadcast if the object is not valid
     * @param arguments Arguments to the message
     * @return True if the {@link Validatable} is valid
     */
    default boolean isValidOr(Validatable validatable, String message, Object... arguments)
    {
        if (!ensureNotNull(validatable).isValid(this))
        {
            problem(message, arguments);
            return false;
        }
        return true;
    }
}
