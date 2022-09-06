package com.telenav.kivakit.interfaces.collection;

import java.util.function.Function;

public interface Joinable<Element> extends
        Iterable<Element>,
        Sized
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
     * @return The elements in this sequence joined as a string with the given separator or the default value if this
     * sequence is empty
     */
    default String join(String separator, String defaultValue)
    {
        if (isEmpty())
        {
            return defaultValue;
        }
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
}
