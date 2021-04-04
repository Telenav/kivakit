////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.property;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A filter that determines how a {@link Property} can be accessed.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public interface PropertyFilter
{
    /** True if the given method should be included as a property getter */
    boolean includeAsGetter(Method method);

    /** True if the given method should be included as a property setter */
    boolean includeAsSetter(Method method);

    /** True if the given field should be included */
    boolean includeField(Field field);

    /** The name of the field */
    String nameForField(Field field);

    /** A non-camelcase name of the method */
    String nameForMethod(Method method);
}
