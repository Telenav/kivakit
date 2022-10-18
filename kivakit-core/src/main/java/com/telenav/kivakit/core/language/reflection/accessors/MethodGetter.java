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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.language.reflection.ReflectionProblem;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Gets the value of a property by calling its getter method
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MethodGetter implements Getter
{
    /** The method to call to get a value */
    private final Method method;

    public MethodGetter(Method method)
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

    /**
     * Gets the value returned by this getter in the given object
     *
     * @param object The object to get from
     * @return The value of this property in the given object, or an instance of {@link ReflectionProblem} if the
     * operation failed
     */
    @Override
    public Object get(Object object)
    {
        try
        {
            var problem = method.makeAccessible();
            if (problem == null)
            {
                return method.invoke(object);
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
    public String toString()
    {
        return "[MethodGetter name = " + name() + ", type = " + type() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<?> type()
    {
        return method.parentType();
    }
}
