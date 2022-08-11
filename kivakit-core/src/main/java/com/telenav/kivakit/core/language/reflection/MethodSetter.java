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
import java.lang.reflect.Method;

@UmlClassDiagram(diagram = DiagramReflection.class)
public class MethodSetter implements Setter
{
    private final transient Method method;

    public MethodSetter(Method method)
    {
        this.method = method;
    }

    @Override
    public <T extends Annotation> T annotation(Class<T> annotationType)
    {
        return method.getAnnotation(annotationType);
    }

    @Override
    public String name()
    {
        return method.getName();
    }

    @Override
    public ReflectionProblem set(Object object, Object value)
    {
        try
        {
            method.invoke(object, value);
            return null;
        }
        catch (Exception e)
        {
            return new ReflectionProblem(e, "Cannot set: " + this);
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
