package com.telenav.kivakit.interfaces.numeric;

public interface Arithmetic<Value>
{
    Value dividedBy(Value value);

    Value minus(Value value);

    Value plus(Value value);

    Value times(Value value);
}
