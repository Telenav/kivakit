package com.telenav.kivakit.interfaces.numeric;

public interface QuantumComparable<Value extends Quantizable> extends Quantizable
{
    default boolean isApproximately(Quantizable that, Quantizable within)
    {
        return Math.abs(quantum() - that.quantum()) <= within.quantum();
    }

    default boolean isGreaterThan(Quantizable that)
    {
        return quantum() > that.quantum();
    }

    default boolean isGreaterThanOrEqualTo(Quantizable that)
    {
        return quantum() >= that.quantum();
    }

    default boolean isLessThan(Quantizable that)
    {
        return quantum() < that.quantum();
    }

    default boolean isLessThanOrEqualTo(Quantizable that)
    {
        return quantum() <= that.quantum();
    }
}
