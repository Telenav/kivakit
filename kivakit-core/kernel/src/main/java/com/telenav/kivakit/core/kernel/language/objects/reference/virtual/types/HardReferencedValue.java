////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.objects.reference.virtual.types;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageObjectReference;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

@UmlClassDiagram(diagram = DiagramLanguageObjectReference.class)
@UmlExcludeSuperTypes({ Source.class })
public class HardReferencedValue<T> implements Source<T>
{
    private final T value;

    public HardReferencedValue(final T value)
    {
        this.value = value;
    }

    @Override
    public T get()
    {
        return value;
    }
}
