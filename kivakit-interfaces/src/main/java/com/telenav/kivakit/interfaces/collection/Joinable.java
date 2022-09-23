package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.CodeQuality;

import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * An object that is {@link Joinable} is {@link Iterable}. Objects are iterated through and joined by separator values
 * into a String.
 *
 * <p><b>Methods</b></p>
 *
 * <ul>
 *     <li>{@link #join(char separator)} - Separates the elements in this {@link Iterable} with the given character separator</li>
 *     <li>{@link #join(String separator)} - Separates the elements in this {@link Iterable} with the given string separator</li>
 *     <li>{@link #joinOrDefault(String separator, String defaultValue)} - Separates the elements in this {@link Iterable} with
 *         the given string. If there are no elements the default value is returned.</li>
 *     <li>{@link #join(String separator, Function)} - Separates the elements in this {@link Iterable} with the
 *         given separator. Each element is transformed into a string using the given function</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = COMPLETE)
public interface Joinable<Element> extends Iterable<Element>
{
    /**
     * @return The elements in this sequence joined as a string with the given separator
     */
    default String join(char separator)
    {
        return join(String.valueOf(separator));
    }

    /**
     * @return The elements in this sequence joined as a string with the given separator
     */
    default String join(String separator)
    {
        return join(separator, Object::toString);
    }

    /**
     * @return The elements in this sequence transformed into strings by the given function and joined together with the
     * given separator
     */
    default String join(String separator, Function<Element, String> toString)
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
     * @return The elements in this sequence joined as a string with the given separator or the default value if this
     * sequence is empty
     */
    default String joinOrDefault(String separator, String defaultValue)
    {
        if (!iterator().hasNext())
        {
            return defaultValue;
        }
        return join(separator, Object::toString);
    }
}
