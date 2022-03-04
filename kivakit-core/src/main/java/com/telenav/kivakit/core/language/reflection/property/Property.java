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

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.FieldGetter;
import com.telenav.kivakit.core.language.reflection.Getter;
import com.telenav.kivakit.core.language.reflection.Member;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.language.reflection.MethodGetter;
import com.telenav.kivakit.core.language.reflection.ReflectionProblem;
import com.telenav.kivakit.core.language.reflection.Setter;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.project.lexakai.DiagramReflection;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A property with a getter and/or setter
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public class Property implements Named, Comparable<Property>
{
    private transient Getter getter;

    private final String name;

    private transient Setter setter;

    public Property(String name, Getter getter, Setter setter)
    {
        Ensure.ensure(name != null);
        Ensure.ensure(getter != null || setter != null);
        Ensure.ensure(getter == null || setter == null || getter.type().equals(setter.type()));

        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    public ReflectionProblem clear(Object object)
    {
        if (setter != null)
        {
            return setter.set(object, null);
        }
        else
        {
            return new ReflectionProblem("Setter not found: " + this);
        }
    }

    @Override
    public int compareTo(Property that)
    {
        return name.compareTo(that.name);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Property)
        {
            var that = (Property) object;
            return name.equals(that.name);
        }
        return false;
    }

    public Field field()
    {
        var getter = getter();
        if (getter instanceof FieldGetter)
        {
            return new Field(null, ((FieldGetter) getter).field());
        }
        return null;
    }

    /**
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

    public Getter getter()
    {
        return getter;
    }

    public void getter(Getter getter)
    {
        this.getter = getter;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(name);
    }

    public boolean isNull(Object object)
    {
        return get(object) == null;
    }

    public boolean isOptional()
    {
        return setter.hasAnnotation(KivaKitOptionalProperty.class);
    }

    public Member member()
    {
        var method = method();
        if (method == null)
        {
            return field();
        }
        return method;
    }

    public Method method()
    {
        var getter = getter();
        if (getter instanceof MethodGetter)
        {
            return new Method(null, ((MethodGetter) getter).method());
        }
        return null;
    }

    @Override
    public String name()
    {
        return name;
    }

    public <T> ReflectionProblem set(Object object, Source<T> source)
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

    public Setter setter()
    {
        return setter;
    }

    public void setter(Setter setter)
    {
        this.setter = setter;
    }

    @Override
    public String toString()
    {
        return "[Property name = " + name() + ", type = " + type().simpleName() + "]";
    }

    public Type<?> type()
    {
        if (getter != null)
        {
            return Type.forClass(getter.type());
        }
        if (setter != null)
        {
            return Type.forClass(setter.type());
        }
        return null;
    }

    private boolean canAssign(Class<?> from, Class<?> to)
    {
        if (to.isAssignableFrom(from))
        {
            return true;
        }
        if (to.isPrimitive())
        {
            if (to == Integer.TYPE && from == Integer.class)
            {
                return true;
            }
            if (to == Long.TYPE && from == Long.class)
            {
                return true;
            }
            if (to == Character.TYPE && from == Character.class)
            {
                return true;
            }
            if (to == Boolean.TYPE && from == Boolean.class)
            {
                return true;
            }
            if (to == Short.TYPE && from == Short.class)
            {
                return true;
            }
            if (to == Byte.TYPE && from == Byte.class)
            {
                return true;
            }
            if (to == Double.TYPE && from == Double.class)
            {
                return true;
            }
            if (to == Float.TYPE && from == Float.class)
            {
                return true;
            }
        }
        if (from.isPrimitive())
        {
            if (to == Integer.class && from == Integer.TYPE)
            {
                return true;
            }
            if (to == Long.class && from == Long.TYPE)
            {
                return true;
            }
            if (to == Character.class && from == Character.TYPE)
            {
                return true;
            }
            if (to == Boolean.class && from == Boolean.TYPE)
            {
                return true;
            }
            if (to == Short.class && from == Short.TYPE)
            {
                return true;
            }
            if (to == Byte.class && from == Byte.TYPE)
            {
                return true;
            }
            return to == Float.class && from == Float.TYPE;
        }
        return false;
    }
}
