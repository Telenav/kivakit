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
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Provides access to a field of a particular object, or of any object
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Field extends Member
{
    /** The underlying reflection field */
    private final java.lang.reflect.Field field;

    /** The object where the field should be accessed */
    private final Object object;

    /** The type where this field is defined */
    private final Type<?> type;

    /**
     * Constructs a field of a particular object
     */
    public Field(Object object, java.lang.reflect.Field field)
    {
        this.type = Type.type(object);
        this.object = object;
        this.field = field;
    }

    /**
     * Constructs a field of any object
     */
    public Field(Type<?> type, java.lang.reflect.Field field)
    {
        this.type = type;
        this.object = null;
        this.field = field;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Annotation> T annotation(Class<T> annotationClass)
    {
        return field.getAnnotation(annotationClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<Type<T>> arrayElementType()
    {
        if (field.getType().isArray())
        {
            var list = new ObjectList<Type<T>>();
            list.add(Type.typeForClass((Class<T>) field.getType().getComponentType()));
            return list;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Field)
        {
            var that = (Field) object;
            return this.object == that.object && field.equals(that.field);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> ObjectList<Type<T>> genericTypeParameters()
    {
        if (field.getGenericType() instanceof ParameterizedType)
        {
            var list = new ObjectList<Type<T>>();
            var genericType = (ParameterizedType) field.getGenericType();
            for (var at : genericType.getActualTypeArguments())
            {
                if (at instanceof Class)
                {
                    list.add(Type.typeForClass((Class<T>) at));
                }
            }
            return list;
        }
        return null;
    }

    /**
     * Gets the value of this field the given object
     *
     * @return The value or an instance of {@link ReflectionProblem} if something went wrong
     */
    public Object get(@NotNull Object object)
    {
        try
        {
            var problem = makeAccessible();
            if (problem == null)
            {
                return field.get(ensureNotNull(object));
            }
            return problem;
        }
        catch (Exception e)
        {
            return new ReflectionProblem(e, "Cannot get " + this);
        }
    }

    /**
     * Gets the value of this field
     *
     * @return The value or an instance of {@link ReflectionProblem} if something went wrong
     */
    public Object get()
    {
        if (object == null)
        {
            return new ReflectionProblem("No object to get from");
        }
        return get(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Hash.hashMany(System.identityHashCode(object), field);
    }

    /**
     * Returns true if this is a primitive field
     */
    public boolean isPrimitive()
    {
        return field.getType().isPrimitive();
    }

    /**
     * Returns true if this is a static field
     */
    public boolean isStatic()
    {
        return Modifier.isStatic(field.getModifiers());
    }
    /**
     * Returns true if this is a transient field
     */
    public boolean isTransient()
    {
        return Modifier.isTransient(field.getModifiers());
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
            field.setAccessible(true);
            return null;
        }
        catch (Exception e)
        {
            return new ReflectionProblem("Cannot access " + this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return field.getName();
    }

    /**
     * Gets the value of this field the given object
     *
     * @return The value or an instance of {@link ReflectionProblem} if something went wrong
     */
    public Object set(@NotNull Object object, Object value)
    {
        try
        {
            var problem = makeAccessible();
            if (problem == null)
            {
                field.set(ensureNotNull(object), value);
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
     * Gets the value of this field
     *
     * @return The value or an instance of {@link ReflectionProblem} if something went wrong
     */
    public Object set(Object value)
    {
        if (object == null)
        {
            return new ReflectionProblem("No object to set to");
        }
        return set(object, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        if (object == null)
        {
            return field.getName();
        }
        return Classes.simpleName(object.getClass()) + "." + field.getName() + " = " + object;
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
