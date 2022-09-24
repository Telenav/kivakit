package com.telenav.kivakit.interfaces.value;

import org.jetbrains.annotations.NotNull;

public interface DoubleValued extends
        LongValued,
        Comparable<LongValued>
{
    @Override
    default int compareTo(@NotNull LongValued that)
    {
        if (that instanceof DoubleValued)
        {
            return Double.compare(doubleValue(), ((DoubleValued) that).doubleValue());
        }
        return Double.compare(doubleValue(), that.longValue());
    }

    /**
     * @return The long value for this object
     */
    double doubleValue();

    @Override
    default long longValue()
    {
        return (long) doubleValue();
    }
}
