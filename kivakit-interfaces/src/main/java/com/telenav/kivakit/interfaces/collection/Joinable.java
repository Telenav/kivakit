package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * An object that is {@link Joinable} is {@link Iterable}. Objects are iterated through and joined by separator values
 * into a String.
 *
 * <p><b>Methods</b></p>
 *
 * <ul>
 *     <li>{@link #join(char separator)} - Returns a string where each value in this {@link Iterable} is separated from
 *     the next with the given separator</li>
 *     <li>{@link #join(String separator)} - Returns a string where each value in this {@link Iterable} is separated
 *     from the next with the given separator</li>
 *     <li>{@link #joinOrDefault(String separator, String defaultValue)} - Returns a string where each value in this
 *     {@link Iterable} is separated from the next with the given separator. If there are no values the default value
 *     is returned.</li>
 *     <li>{@link #join(String separator, Function)} - Returns a string where each value in this
 *     {@link Iterable} is separated from the next with the given separator. Each value is transformed into a string
 *     using the given function</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Iterable
 * @see Function
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@TypeQuality(stability = STABILITY_UNDETERMINED,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             reviews = 1,
             reviewers = "shibo")
public interface Joinable<Value> extends Iterable<Value>
{
    /**
     * Returns this {@link Iterable} object, joined into a string by {@link #separator()}s.
     */
    default String join()
    {
        return join(separator());
    }

    /**
     * Returns the values in this sequence joined as a string with the given separator
     */
    default String join(char separator)
    {
        return join(String.valueOf(separator));
    }

    /**
     * Returns the elements in this sequence joined as a string with the given separator
     */
    default String join(String separator)
    {
        return join(separator, Object::toString);
    }

    /**
     * Returns the values in this sequence transformed into strings by the given function and joined together with the
     * given separator.
     *
     * @param separator The separator to put between values
     * @param toString The function to convert values into strings
     * @return The joined string
     */
    default String join(String separator, Function<Value, String> toString)
    {
        var builder = new StringBuilder();
        for (var value : this)
        {
            if (builder.length() > 0)
            {
                builder.append(separator);
            }
            builder.append(toString.apply(value));
        }
        return builder.toString();
    }

    /**
     * Returns the values in this sequence joined as a string with the given separator or the default value if this
     * sequence is empty.
     *
     * @param separator The separator to put between values
     * @param defaultValue The value to return if this joinable has nothing to join
     * @return The joined values in this joinable, or the default value if there are none
     */
    default String joinOrDefault(String separator, String defaultValue)
    {
        if (!iterator().hasNext())
        {
            return defaultValue;
        }
        return join(separator, Object::toString);
    }

    /**
     * Returns the separator to use when joining
     */
    default String separator()
    {
        return ", ";
    }
}
