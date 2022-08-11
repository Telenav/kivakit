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

package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

@UmlClassDiagram(diagram = DiagramReflection.class)
public class FieldGetter implements Getter
{
    private final transient java.lang.reflect.Field field;

    public FieldGetter(java.lang.reflect.Field field)
    {
        this.field = field;
    }

    @Override
    public <T extends Annotation> T annotation(Class<T> annotationType)
    {
        return field.getAnnotation(annotationType);
    }

    public java.lang.reflect.Field field()
    {
        return field;
    }

    @Override
    public Object get(Object object)
    {
        try
        {
            if (Field.accessible(field))
            {
                return field.get(object);
            }
            return new ReflectionProblem("Cannot get: " + this);
        }
        catch (Exception e)
        {
            return new ReflectionProblem(e, "Cannot get: " + this);
        }
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
