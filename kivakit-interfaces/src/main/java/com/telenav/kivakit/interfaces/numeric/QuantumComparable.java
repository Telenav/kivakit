package com.telenav.kivakit.interfaces.numeric;

public interface QuantumComparable<Value extends Quantizable> extends Quantizable
{
    default boolean isApproximately(Value that, Value within)
    {
        return Math.abs(quantum() - that.quantum()) <= within.quantum();
    }

    default boolean isGreaterThan(Value that)
    {
        return quantum() > that.quantum();
    }

    default boolean isGreaterThanOrEqualTo(Value that)
    {
        return quantum() >= that.quantum();
    }

    default boolean isLessThan(Value that)
    {
        return quantum() < that.quantum();
    }

    default boolean isLessThanOrEqualTo(Value that)
    {
        return quantum() <= that.quantum();
    }
}

