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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
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

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Provides access to a field of a particular object, or of any object
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramReflection.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Field extends Member
{
    /** The underlying reflection field */
    private final java.lang.reflect.Field field;

    /** The object where the field should be accessed */
    private Object parentObject;

    /** The type where this field is defined */
    private final Type<?> parentType;

    /**
     * Constructs a field of a particular object
     */
    public Field(Object parentObject, java.lang.reflect.Field field)
    {
        this.parentType = Type.type(parentObject);
        this.parentObject = parentObject;
        this.field = field;
    }

    /**
     * Constructs a field of any object
     */
    public Field(Type<?> parentType, java.lang.reflect.Field field)
    {
        this.parentType = parentType;
        this.parentObject = null;
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
            return this.parentObject == that.parentObject && field.equals(that.field);
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
     * @return he value or an instance of {@link ReflectionProblem} if something went wrong
     */
    public Object get()
    {
        if (parentObject == null)
        {
            return new ReflectionProblem("No object to get from");
        }
        return get(parentObject);
    }

    /**
     * Returns true if this field has an annotation of the given type
     */
    public boolean hasAnnotation(Class<? extends Annotation> type)
    {
        return field.isAnnotationPresent(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Hash.hashMany(System.identityHashCode(parentObject), field);
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
     * Returns true if this is a synthetic field
     */
    public boolean isSynthetic()
    {
        return field.isSynthetic();
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
     * Sets the object for this field
     *
     * @param object The object
     */
    public void object(Object object)
    {
        this.parentObject = object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<?> parentType()
    {
        return parentType;
    }

    /**
     * Sets the value of this field on the given object
     *
     * @return The value or an instance of {@link ReflectionProblem} if something went wrong
     */
    public ReflectionProblem set(@NotNull Object object, Object value)
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
        if (parentObject == null)
        {
            return new ReflectionProblem("No object to set to");
        }
        return set(parentObject, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        if (parentObject == null)
        {
            return field.getName();
        }
        return Classes.simpleName(parentObject.getClass()) + "." + field.getName() + " = " + parentObject;
    }

    /**
     * Returns the type of this field
     */
    public Type<?> type()
    {
        return Type.typeForClass(field.getType());
    }
}
