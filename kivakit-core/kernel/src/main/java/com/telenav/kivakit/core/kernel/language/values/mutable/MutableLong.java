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
public class MutableLong extends MutableValue<Long>
{
    public MutableLong()
    {
    }

    public MutableLong(final Long value)
    {
        super(value);
    }

    public void maximum(final long value)
    {
        set(Math.max(get(), value));
    }

    public void minimum(final long value)
    {
        set(Math.min(get(), value));
    }
}
