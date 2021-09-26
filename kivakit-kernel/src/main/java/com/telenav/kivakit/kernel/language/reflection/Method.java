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

import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Holds a class and method name when a proper Java reflection {@link java.lang.reflect.Method} cannot be determined, as
 * in the case of the limited and poorly designed {@link StackTraceElement} class.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class Method extends Member
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return A {@link Method} instance for the given stack frame
     */
    public static Method of(final StackTraceElement frame)
    {
        try
        {
            final var type = Class.forName(frame.getClassName());
            return new Method(type, frame.getMethodName());
        }
        catch (final Exception e)
        {
            LOGGER.problem(e, "Unable to find method for: $", frame);
        }
        return null;
    }

    private final Class<?> type;

    private java.lang.reflect.Method method;

    private final String name;

    public Method(final Class<?> type, final java.lang.reflect.Method method)
    {
        this.type = ensureNotNull(type);
        this.method = ensureNotNull(method);
        this.name = method.getName();
    }

    public Method(final Class<?> type, String name)
    {
        this.type = ensureNotNull(type);
        this.name = ensureNotNull(name);
    }

    public List<Type<?>> genericTypeParameters()
    {
        var list = new ArrayList<Type<?>>();
        var genericType = (ParameterizedType) method.getGenericReturnType();
        for (var at : genericType.getActualTypeArguments())
        {
            list.add(Type.forClass((Class<?>) at));
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
