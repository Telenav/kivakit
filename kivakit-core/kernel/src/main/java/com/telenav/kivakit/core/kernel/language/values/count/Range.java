////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.count;

import com.telenav.kivakit.core.kernel.interfaces.numeric.Maximizable;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Minimizable;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Ranged;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Range<T extends Minimizable<T> & Maximizable<T>> implements Ranged<T>
{
    private final T minimum;

    private final T maximum;

    public Range(final T minimum, final T maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public T constrainTo(final T value)
    {
        return maximum.minimum(minimum.maximum(value));
    }

    @Override
    public boolean contains(final T value)
    {
        return false;
    }

    @Override
    public T maximum()
    {
        return maximum;
    }

    @Override
    public T minimum()
    {
        return minimum;
    }
}
