package com.telenav.kivakit.kernel.interfaces.time;

import com.telenav.kivakit.kernel.language.time.Time;

public interface CreatedAt
{
    Time created();

    default boolean wasCreatedAfter(CreatedAt that)
    {
        return created().isAfter(that.created());
    }

    default boolean wasCreatedBefore(CreatedAt that)
    {
        return created().isBefore(that.created());
    }
}
