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

package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Member;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.language.reflection.ReflectionProblem;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.accessors.FieldGetter;
import com.telenav.kivakit.core.language.reflection.accessors.FieldSetter;
import com.telenav.kivakit.core.language.reflection.accessors.Getter;
import com.telenav.kivakit.core.language.reflection.accessors.MethodGetter;
import com.telenav.kivakit.core.language.reflection.accessors.MethodSetter;
import com.telenav.kivakit.core.language.reflection.accessors.Setter;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.language.Hash.hashMany;

/**
 * A property with a getter and/or setter.
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #get(Object)}</li>
 *     <li>{@link #set(Object, Supplier)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #field()}</li>
 *     <li>{@link #getter()}</li>
 *     <li>{@link #getter(Getter)}</li>
 *     <li>{@link #isOptional()}</li>
 *     <li>{@link #member()}</li>
 *     <li>{@link #method()}</li>
 *     <li>{@link #name()}</li>
 *     <li>{@link #setter()}</li>
 *     <li>{@link #setter(Setter)}</li>
 *     <li>{@link #parentType()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused" })
@UmlClassDiagram(diagram = DiagramReflection.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Property implements Named, Comparable<Property>
{
    /** The property getter */
    private Getter getter;

    /** The name of the property */
    private final String name;

    /** Any property setter */
    private Setter setter;

    /**
     * Constructs a property with the given name, getter and setter
     *
     * @param name The name of the property
     * @param getter The property getter
     * @param setter Any property setter
     */
    public Property(String name, Getter getter, Setter setter)
    {
        ensure(name != null);
        ensure(getter != null || setter != null);
        ensure(getter == null || setter == null || getter.type().equals(setter.type()));

        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Property that)
    {
        return name.compareTo(that.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Property that)
        {
            return name.equals(that.name);
        }
        return false;
    }

    /**
     * Returns any field for this property, or null if there is none
     */
    public Field field()
    {
        var getter = getter();
        if (getter instanceof FieldGetter)
        {
            return ((FieldGetter) getter).field();
        }
        var setter = setter();
        if (setter instanceof FieldSetter)
        {
            return ((FieldSetter) setter).field();
        }
        return null;
    }

    /**
     * Retrieves this property from the given object
     *
     * @param object The object to get from
     * @return The object retrieved or a String if something went wrong
     */
    public Object get(Object object)
    {
        if (getter != null && object != null)
        {
            return getter.get(object);
        }
        return null;
    }

    /**
     * Returns the getter for this property
     */
    public Getter getter()
    {
        return getter;
    }

    /**
     * Changes the getter for this property
     */
    public void getter(Getter getter)
    {
        this.getter = getter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return hashMany(name);
    }

    /**
     * Returns true if this property is optional because it was annotated with {@link OptionalProperty}
     */
    public boolean isOptional()
    {
        return setter.hasAnnotation(OptionalProperty.class);
    }

    /**
     * Returns the field or method that is getting this property
     */
    public Member member()
    {
        var method = method();
        if (method == null)
        {
            return field();
        }
        return method;
    }

    /**
     * Returns the method that gets this property
     */
    public Method method()
    {
        var getter = getter();
        if (getter instanceof MethodGetter)
        {
            return ((MethodGetter) getter).method();
        }
        var setter = setter();
        if (setter instanceof MethodSetter)
        {
            return ((MethodSetter) setter).method();
        }
        return null;
    }

    /**
     * Returns the name of this property
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * Returns the type for which this property is defined
     */
    public Type<?> parentType()
    {
        if (getter != null)
        {
            return getter.type();
        }
        if (setter != null)
        {
            return setter.type();
        }
        return null;
    }

    /**
     * Sets this property on the given object using the value supplied by the given source
     *
     * @param object The object to set the property on
     * @param source The source of the value
     * @return ReflectionProblem if anything went wrong, otherwise null
     */
    public <T> ReflectionProblem set(Object object, Supplier<T> source)
    {
        var value = source.get();

        if (value == null && !isOptional())
        {
            return new ReflectionProblem("Required property was not populated: " + this);
        }

        if (setter != null)
        {
            return setter.set(object, value);
        }
        else
        {
            return new ReflectionProblem("Setter not found: " + this);
        }
    }

    /**
     * Returns any setter for this property
     */
    public Setter setter()
    {
        return setter;
    }

    /**
     * Changes the setter for this property
     */
    public void setter(Setter setter)
    {
        this.setter = setter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "[Property name = " + name() + ", type = " + parentType().simpleName() + "]";
    }
}
