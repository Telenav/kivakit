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

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.project.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Holds a class and method name when a proper Java reflection {@link java.lang.reflect.Method} cannot be determined, as
 * in the case of the limited and poorly designed {@link StackTraceElement} class.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public class Method extends Member
{
    /**
     * @return A {@link Method} instance for the given stack frame
     */
    public static Method of(StackTraceElement frame)
    {
        try
        {
            var type = Class.forName(frame.getClassName());
            return new Method(type, frame.getMethodName());
        }
        catch (Exception ignored)
        {
        }
        return null;
    }

    private java.lang.reflect.Method method;

    private final String name;

    private final Class<?> type;

    public Method(Class<?> type, java.lang.reflect.Method method)
    {
        this.type = ensureNotNull(type);
        this.method = ensureNotNull(method);
        name = method.getName();
    }

    public Method(Class<?> type, String name)
    {
        this.type = ensureNotNull(type);
        this.name = ensureNotNull(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<Type<T>> arrayElementType()
    {
        if (method.getReturnType().isArray())
        {
            var list = new ObjectList<Type<T>>();
            list.add(Type.forClass((Class<T>) method.getReturnType().getComponentType()));
            return list;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> List<Type<T>> genericTypeParameters()
    {
        var list = new ArrayList<Type<T>>();
        var genericType = (ParameterizedType) method.getGenericReturnType();
        for (var at : genericType.getActualTypeArguments())
        {
            if (at instanceof Class)
            {
                list.add(Type.forClass((Class<T>) at));
            }
        }
        return list;
    }

    public java.lang.reflect.Method method()
    {
        return method;
    }

    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return typeClass().getSimpleName() + "." + name() + "()";
    }

    public Type<?> type()
    {
        return Type.forClass(typeClass());
    }

    public Class<?> typeClass()
    {
        return type;
    }
}
