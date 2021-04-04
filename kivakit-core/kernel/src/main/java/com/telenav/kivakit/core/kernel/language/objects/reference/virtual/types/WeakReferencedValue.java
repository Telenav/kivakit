////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.objects.reference.virtual.types;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageObjectReference;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

@UmlClassDiagram(diagram = DiagramLanguageObjectReference.class)
@UmlExcludeSuperTypes({ Source.class, Named.class })
public class WeakReferencedValue<T> extends WeakReference<T> implements Source<T>, Named
{
    private final String name;

    public WeakReferencedValue(final String name, final T value, final ReferenceQueue<T> queue)
    {
        super(value, queue);
        this.name = name;
    }

    @Override
    public T get()
    {
        return super.get();
    }

    @Override
    public String name()
    {
        return name;
    }
}
