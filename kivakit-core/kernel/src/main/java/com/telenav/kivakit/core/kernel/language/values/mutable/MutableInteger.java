////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.mutable;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class MutableInteger extends MutableValue<Integer>
{
    public MutableInteger()
    {
    }

    public MutableInteger(final Integer value)
    {
        super(value);
    }

    public void maximum(final int value)
    {
        set(Math.max(get(), value));
    }

    public void minimum(final int value)
    {
        set(Math.min(get(), value));
    }
}
