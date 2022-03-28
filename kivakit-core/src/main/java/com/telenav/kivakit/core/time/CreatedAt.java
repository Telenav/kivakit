package com.telenav.kivakit.core.time;

public interface CreatedAt
{
    default Duration age()
    {
        return created().elapsedSince();
    }

    default Time created()
    {
        throw new UnsupportedOperationException("Cannot retrieve last modified time from: " + getClass());
    }

    default boolean wasCreatedAfter(CreatedAt that)
    {
        return created().isAfter(that.created());
    }

    default boolean wasCreatedBefore(CreatedAt that)
    {
        return created().isBefore(that.created());
    }
}
