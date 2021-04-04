////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.access.method;

import com.telenav.kivakit.core.kernel.language.reflection.access.Setter;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Failure;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Success;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.messaging.Message;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class MethodSetter implements Setter
{
    private final transient Method method;

    public MethodSetter(final Method method)
    {
        this.method = method;
    }

    @Override
    public <T extends Annotation> T annotation(final Class<T> annotationType)
    {
        return method.getAnnotation(annotationType);
    }

    @Override
    public String name()
    {
        return method.getName();
    }

    @Override
    public Message set(final Object object, final Object value)
    {
        try
        {
            method.invoke(object, value);
            return Success.INSTANCE;
        }
        catch (final Exception e)
        {
            return new Failure("Cannot set ${debug}", this);
        }
    }

    @Override
    public String toString()
    {
        return "[MethodSetter name = " + name() + ", type = " + type() + "]";
    }

    @Override
    public Class<?> type()
    {
        return method.getParameterTypes()[0];
    }
}
