////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.access.field;

import com.telenav.kivakit.core.kernel.language.reflection.Field;
import com.telenav.kivakit.core.kernel.language.reflection.access.Getter;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class FieldGetter implements Getter
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private final transient java.lang.reflect.Field field;

    public FieldGetter(final java.lang.reflect.Field field)
    {
        this.field = field;
    }

    @Override
    public <T extends Annotation> T annotation(final Class<T> annotationType)
    {
        return field.getAnnotation(annotationType);
    }

    @Override
    public Object get(final Object object)
    {
        try
        {
            if (Field.accessible(field))
            {
                return field.get(object);
            }
            LOGGER.warning("Cannot get ${debug}", this);
        }
        catch (final Exception e)
        {
            LOGGER.warning(e, "Cannot get ${debug}", this);
        }
        return null;
    }

    @Override
    public String name()
    {
        return field.getName();
    }

    @Override
    public String toString()
    {
        return "[FieldGetter name = " + field.getName() + ", type = " + type() + "]";
    }

    @Override
    public Class<?> type()
    {
        return field.getType();
    }
}
