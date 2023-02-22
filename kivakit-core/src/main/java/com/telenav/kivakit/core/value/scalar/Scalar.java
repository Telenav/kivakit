package com.telenav.kivakit.core.value.scalar;

import com.telenav.kivakit.interfaces.value.DoubleValued;

public class Scalar implements DoubleValued
{
    public static Scalar scalar(double value)
    {
        return new Scalar(value);
    }

    private final double value;

    protected Scalar(double value)
    {
        this.value = value;
    }

    @Override
    public double doubleValue()
    {
        return value;
    }
}
