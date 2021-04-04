////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.access.field;

import com.telenav.kivakit.core.kernel.language.reflection.Field;
import com.telenav.kivakit.core.kernel.language.reflection.access.Setter;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Failure;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.messaging.Message;

import java.lang.annotation.Annotation;

@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class FieldSetter implements Setter
{
    private final transient java.lang.reflect.Field field;

    public FieldSetter(final java.lang.reflect.Field field)
    {
        this.field = field;
    }

    @Override
    public <T extends Annotation> T annotation(final Class<T> annotationType)
    {
        return field.getAnnotation(annotationType);
    }

    @Override
    public String name()
    {
        return field.getName();
    }

    @Override
    public Message set(final Object object, final Object value)
    {
        try
        {
            if (Field.accessible(field))
            {
                field.set(object, value);
                return OperationSucceeded.INSTANCE;
            }
            else
            {
                return new Failure("Cannot set ${debug}", this);
            }
        }
        catch (final Exception e)
        {
            return new Failure(e, "Cannot set ${debug}", this);
        }
    }

    @Override
    public String toString()
    {
        return "[FieldSetter name = " + name() + ", type = " + type() + "]";
    }

    @Override
    public Class<?> type()
    {
        return field.getType();
    }
}
