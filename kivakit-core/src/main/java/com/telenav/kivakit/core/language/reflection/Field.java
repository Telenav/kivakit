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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.language.Classes.simpleName;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;

/**
 * Provides access to a field of a particular object, or of any object.
 *
 * <p><b>Field Access</b></p>
 *
 * <ul>
 *     <li>{@link #get()} - Reads this field's value on any object passed to the constructor</li>
 *     <li>{@link #get(Object)} - Reads this field's value on the given object</li>
 *     <li>{@link #makeAccessible()} - Makes the field accessible even if it is private</li>
 *     <li>{@link #object(Object)} - Sets the object to access when calling {@link #get()} and {@link #set(Object)}</li>
 *     <li>{@link #set(Object)} - Writes the given value to this field of any object passed to the constructor</li>
 *     <li>{@link #set(Object, Object)} - Writes the given value to this field of the given object</li>
 *     <li>{@link #type()} - Returns the type of this field</li>
 * </ul>
 *
 * <p><b>Annotations</b></p>
 *
 * <ul>
 *     <li>{@link #annotation(Class)}</li>
 *     <li>{@link #hasAnnotation(Class)}</li>
 * </ul>
 *
 * <p><b>Modifiers</b></p>
 *
 * <ul>
 *     <li>{@link #isFinal()}</li>
 *     <li>{@link #isPrimitive()}</li>
 *     <li>{@link #isPrivate()}</li>
 *     <li>{@link #isProtected()}</li>
 *     <li>{@link #isPublic()}</li>
 *     <li>{@link #isStatic()}</li>
 *     <li>{@link #isSynthetic()}</li>
 *     <li>{@link #isTransient()}</li>
 *     <li>{@link #isVolatile()}</li>
 *     <li>{@link #modifiers()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #arrayElementType()}</li>
 *     <li>{@link #genericTypeParameters()}</li>
 *     <li>{@link #name()}</li>
 *     <li>{@link #parentType()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see ReflectionProblem
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramReflection.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
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
    protected Field(Object parentObject, java.lang.reflect.Field field)
    {
        this.parentType = Type.type(parentObject);
        this.parentObject = parentObject;
        this.field = field;
    }

    /**
     * Constructs a field of any object
     */
    protected Field(Type<?> parentType, java.lang.reflect.Field field)
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
    public <T> ObjectList<Type<T>> arrayElementType()
    {
        if (field.getType().isArray())
        {
            var list = new ObjectList<Type<T>>();
            list.add(typeForClass((Class<T>) field.getType().getComponentType()));
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
        if (object instanceof Field that)
        {
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
        if (field.getGenericType() instanceof ParameterizedType genericType)
        {
            var list = new ObjectList<Type<T>>();
            for (var at : genericType.getActualTypeArguments())
            {
                if (at instanceof Class)
                {
                    list.add(typeForClass((Class<T>) at));
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
        if (parentObject == null)
        {
            return new ReflectionProblem("No object to get from");
        }
        return get(parentObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return hashMany(System.identityHashCode(parentObject), field);
    }

    /**
     * Returns true if this is a primitive field
     */
    public boolean isPrimitive()
    {
        return field.getType().isPrimitive();
    }

    /**
     * Returns true if this is a synthetic field
     */
    @Override
    public boolean isSynthetic()
    {
        return field.isSynthetic();
    }

    /**
     * Returns true if this is a transient field
     */
    public boolean isTransient()
    {
        return Modifier.isTransient(modifiers());
    }

    /**
     * Returns true if this is a synchronized method
     */
    public boolean isVolatile()
    {
        return Modifier.isVolatile(modifiers());
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

    @Override
    public int modifiers()
    {
        return field.getModifiers();
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
        return simpleName(parentObject.getClass()) + "." + field.getName() + " = " + parentObject;
    }

    /**
     * Returns the type of this field
     */
    public Type<?> type()
    {
        return typeForClass(field.getType());
    }
}
