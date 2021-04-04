////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.access.method;

import com.telenav.kivakit.core.kernel.language.reflection.access.Getter;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class MethodGetter implements Getter
{
    private final transient Method method;

    public MethodGetter(final Method method)
    {
        this.method = method;
    }

    @Override
    public <T extends Annotation> T annotation(final Class<T> annotationType)
    {
        return method.getAnnotation(annotationType);
    }

    @Override
    public Object get(final Object object)
    {
        try
        {
            method.setAccessible(true);
            return method.invoke(object);
        }
        catch (final Exception e)
        {
            return new Warning("Cannot get ${debug}", this).toString();
        }
    }

    @Override
    public String name()
    {
        return method.getName();
    }

    @Override
    public String toString()
    {
        return "[MethodGetter name = " + name() + ", type = " + type() + "]";
    }

    @Override
    public Class<?> type()
    {
        return method.getReturnType();
    }
}
