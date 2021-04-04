////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.mutable;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Function;

@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class MutableValue<T>
{
    private T value;

    public MutableValue()
    {
    }

    public MutableValue(final T value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof MutableValue)
        {
            final var that = (MutableValue<?>) object;
            return value.equals(that.value);
        }
        return false;
    }

    public T get()
    {
        return value;
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    public void set(final T value)
    {
        this.value = value;
    }

    public void update(final Function<T, T> updater)
    {
        value = updater.apply(value);
    }
}
