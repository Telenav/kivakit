////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.access;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public interface Getter extends Named
{
    <T extends Annotation> T annotation(final Class<T> annotationType);

    /**
     * @param object The object to get from
     * @return The object retrieved or null if something went wrong
     */
    Object get(final Object object);

    Class<?> type();
}
