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

package com.telenav.kivakit.core.language.reflection.access.field;

import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.ReflectionError;
import com.telenav.kivakit.core.language.reflection.access.Setter;
import com.telenav.kivakit.core.project.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

@UmlClassDiagram(diagram = DiagramReflection.class)
public class FieldSetter implements Setter
{
    private final transient java.lang.reflect.Field field;

    public FieldSetter(java.lang.reflect.Field field)
    {
        this.field = field;
    }

    @Override
    public <T extends Annotation> T annotation(Class<T> annotationType)
    {
        return field.getAnnotation(annotationType);
    }

    @Override
    public String name()
    {
        return field.getName();
    }

    @Override
    public ReflectionError set(Object object, Object value)
    {
        try
        {
            if (Field.accessible(field))
            {
                field.set(object, value);
                return null;
            }
            else
            {
                return new ReflectionError("Cannot set: " + this);
            }
        }
        catch (Exception e)
        {
            return new ReflectionError(e, "Cannot set: " + this);
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
