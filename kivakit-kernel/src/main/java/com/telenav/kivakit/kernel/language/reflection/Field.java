////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.reflection;

import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class Field extends Member
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    public static boolean accessible(final java.lang.reflect.Field field)
    {
        try
        {
            field.setAccessible(true);
            return true;
        }
        catch (final Exception e)
        {
            DEBUG.trace("Unable to access field $.$", field.getDeclaringClass(), field.getName());
        }
        return false;
    }

    private final Object object;

    private final java.lang.reflect.Field field;

    public Field(final Object object, final java.lang.reflect.Field field)
    {
        this.object = object;
        this.field = field;
    }

    public <T extends Annotation> T annotation(final Class<T> annotationClass)
    {
        return field.getAnnotation(annotationClass);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Field)
        {
            final var that = (Field) object;
            return this.object == that.object && field.equals(that.field);
        }
        return false;
    }

    public List<Type<?>> genericTypeParameters()
    {
        var list = new ArrayList<Type<?>>();
        var genericType = (ParameterizedType) field.getGenericType();
        for (var at : genericType.getActualTypeArguments())
        {
            list.add(Type.forClass((Class<?>) at));
        }
        return list;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(System.identityHashCode(object), field);
    }

    public boolean isPrimitive()
    {
        return field.getType().isPrimitive();
    }

    @Override
    public String name()
    {
        return field.getName();
    }

    @Override
    public String toString()
    {
        return Classes.simpleName(object.getClass()) + "." + field.getName() + " = " + object;
    }

    public Type<?> type()
    {
        return Type.forClass(field.getType());
    }

    public Object value()
    {
        // Ensure that the field can be accessed,
        if (accessible(field))
        {
            // and that it's not static,
            if (!Modifier.isStatic(field.getModifiers()))
            {
                // then return the field value.
                try
                {
                    return field.get(object);
                }
                catch (final IllegalArgumentException | IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
