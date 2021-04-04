////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.property.filters.field;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitProperties;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * A filter that includes all non-static fields
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class AllFields extends KivaKitProperties
{
    /**
     * @param included Set of fields and properties to include
     */
    public AllFields(final Set<Include> included)
    {
        super(included);
    }

    @Override
    public boolean includeAsGetter(final Method method)
    {
        return false;
    }

    @Override
    public boolean includeAsSetter(final Method method)
    {
        return false;
    }

    @Override
    public boolean includeField(final Field field)
    {
        return !Modifier.isStatic(field.getModifiers());
    }
}
