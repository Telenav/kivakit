////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.property.filters.method;

import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * This filter matches a field with a particular name
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class NamedMethod extends AllMethods
{
    private final String name;

    public NamedMethod(final String name)
    {
        super(Collections.emptySet());
        this.name = name;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof NamedMethod)
        {
            final var that = (NamedMethod) object;
            return name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(name);
    }

    @Override
    public boolean includeAsGetter(final Method method)
    {
        return super.includeAsGetter(method) && method.getName().equals(name);
    }
}
