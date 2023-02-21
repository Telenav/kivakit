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

package com.telenav.kivakit.core.language.reflection.accessors;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.language.reflection.ReflectionProblem;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;

/**
 * Set the value of a property by calling its setter method
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class MethodSetter implements Setter
{
    /** The method to call to set a value */
    private final Method method;

    public MethodSetter(Method method)
    {
        this.method = method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Annotation> T annotation(Class<T> annotationType)
    {
        return method.annotation(annotationType);
    }

    public Method method()
    {
        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return method.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReflectionProblem set(Object object, Object value)
    {
        try
        {
            var problem = method.makeAccessible();
            if (problem == null)
            {
                method.invoke(object, value);
                return null;
            }
            return problem;
        }
        catch (Exception e)
        {
            return new ReflectionProblem(e, "Cannot get " + this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "[MethodSetter name = " + name() + ", type = " + type() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<?> type()
    {
        return typeForClass(method.parameterTypes()[0]);
    }
}
