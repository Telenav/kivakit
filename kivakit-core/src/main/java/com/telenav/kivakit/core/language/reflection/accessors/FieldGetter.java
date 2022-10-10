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
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.ReflectionProblem;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Gets the value of a field
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class FieldGetter implements Getter
{
    /** The field to access */
    private final Field field;

    public FieldGetter(Field field)
    {
        this.field = field;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Annotation> T annotation(Class<T> annotationType)
    {
        return field.annotation(annotationType);
    }

    /**
     * Returns the field to access
     */
    public Field field()
    {
        return field;
    }

    /**
     * Gets the value of this field in the given object
     *
     * @param object The object to get from
     * @return The value of this field in the given object, or an instance of {@link ReflectionProblem} if the operation
     * failed
     */
    @Override
    public Object get(Object object)
    {
        try
        {
            var problem = field.makeAccessible();
            if (problem == null)
            {
                return field.get(object);
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
    public String name()
    {
        return field.name();
    }

    @Override
    public String toString()
    {
        return "[FieldGetter name = " + name() + ", type = " + type() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<?> type()
    {
        return field.parentType();
    }
}
