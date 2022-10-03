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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.collections.list.ObjectList.objectList;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Holds a class and method name when a proper Java reflection {@link java.lang.reflect.Method} cannot be determined, as
 * in the case of the limited and poorly designed {@link StackTraceElement} class.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class Method extends Member
{
    /**
     * @return A {@link Method} instance for the given stack frame
     */
    public static Method method(StackTraceElement stackFrame)
    {
        try
        {
            var type = Class.forName(stackFrame.getClassName());
            return new Method(Type.typeForClass(type), stackFrame.getMethodName());
        }
        catch (Exception ignored)
        {
        }
        return null;
    }

    /** The underlying reflection method */
    private java.lang.reflect.Method method;

    /** The name of this method */
    private final String name;

    /** The method type */
    private final Type<?> type;

    public Method(Type<?> type, java.lang.reflect.Method method)
    {
        this.type = ensureNotNull(type);
        this.method = ensureNotNull(method);
        name = method.getName();
    }

    public Method(Type<?> type, String name)
    {
        this.type = ensureNotNull(type);
        this.name = ensureNotNull(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Annotation> T annotation(Class<T> annotationClass)
    {
        return method.getAnnotation(annotationClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<Type<T>> arrayElementType()
    {
        if (method.getReturnType().isArray())
        {
            var list = new ObjectList<Type<T>>();
            list.add(Type.typeForClass((Class<T>) method.getReturnType().getComponentType()));
            return list;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<Type<T>> genericTypeParameters()
    {
        var list = new ArrayList<Type<T>>();
        var genericType = (ParameterizedType) method.getGenericReturnType();
        for (var at : genericType.getActualTypeArguments())
        {
            if (at instanceof Class)
            {
                list.add(Type.typeForClass((Class<T>) at));
            }
        }
        return list;
    }

    /**
     * Invokes this method on the given target object
     *
     * @param target The object on which to call the method
     * @param parameters The parameters to pass
     * @return The object returned by the method, or an instance of {@link ReflectionProblem} if the operation failed
     */
    public Object invoke(Object target, Object... parameters)
    {
        try
        {
            var problem = makeAccessible();
            if (problem == null)
            {
                return method.invoke(ensureNotNull(target), parameters);
            }
            return problem;
        }
        catch (Exception e)
        {
            return new ReflectionProblem(e, "Cannot get " + this);
        }
    }

    /**
     * Returns true if this is a static method
     */
    public boolean isPublic()
    {
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * Returns true if this is a static method
     */
    public boolean isStatic()
    {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * Returns true if this is a synthetic method
     */
    public boolean isSynthetic()
    {
        return method.isSynthetic();
    }

    /**
     * Makes this field accessible via reflection
     *
     * @return Any {@link ReflectionProblem} that occurred, or null if the operation succeeded
     */
    public ReflectionProblem makeAccessible()
    {
        try
        {
            method.setAccessible(true);
            return null;
        }
        catch (Exception e)
        {
            return new ReflectionProblem("Cannot access " + this);
        }
    }

    /**
     * Returns the underlying reflection method
     */
    public java.lang.reflect.Method method()
    {
        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * Returns the parameter types for this method
     */
    public Class<?>[] parameterTypes()
    {
        return method.getParameterTypes();
    }

    /**
     * Returns the return type for this method
     */
    public Class<?> returnType()
    {
        return method.getReturnType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return type + "." + name() + "(" + objectList(method.getParameterTypes()).join() + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<?> type()
    {
        return type;
    }
}
