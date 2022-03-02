package com.telenav.kivakit.core.time;

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
