package com.telenav.kivakit.kernel.language.traits;

import com.telenav.kivakit.kernel.messaging.Broadcaster;

public interface LanguageTrait extends Broadcaster
{
    default boolean isNonNullOr(Object object, String message, Object... arguments)
    {
        if (object == null)
        {
            problem(message, arguments);
            return false;
        }
        return true;
    }

    default boolean isTrueOr(boolean truth, String message, Object... arguments)
    {
        if (!truth)
        {
            problem(message, arguments);
        }
        return truth;
    }
}
