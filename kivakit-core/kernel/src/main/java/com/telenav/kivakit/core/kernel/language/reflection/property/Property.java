////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.property;

import com.telenav.kivakit.core.kernel.data.conversion.Converter;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.language.reflection.access.Getter;
import com.telenav.kivakit.core.kernel.language.reflection.access.Setter;
import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyOptional;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Abstracts a property in such a way that we can add direct field access in the future.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class Property implements Named, Comparable<Property>
{
    private final String name;

    private transient Getter getter;

    private transient Setter setter;

    public Property(final String name, final Getter getter, final Setter setter)
    {
        Ensure.ensure(name != null);
        Ensure.ensure(getter != null || setter != null);
        Ensure.ensure(getter == null || setter == null || getter.type().equals(setter.type()));

        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    public Message clear(final Object object)
    {
        if (setter != null)
        {
            return setter.set(object, null);
        }
        else
        {
            return new Warning("$: Setter not found", this);
        }
    }

    @Override
    public int compareTo(final Property that)
    {
        return name.compareTo(that.name);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Property)
        {
            final var that = (Property) object;
            return name.equals(that.name);
        }
        return false;
    }

    /**
     * @param object The object to get from
     * @return The object retrieved or an instance of {@link Message} if something went wrong
     */
    public Object get(final Object object)
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

    public void getter(final Getter getter)
    {
        this.getter = getter;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(name);
    }

    public boolean isNull(final Object object)
    {
        return get(object) == null;
    }

    @Override
    public String name()
    {
        return name;
    }

    @SuppressWarnings({ "unchecked" })
    public Message set(final Listener listener, final Object object, Object value)
    {
        if (value != null)
        {
            // If the type can't be assigned
            if (!canAssign(value.getClass(), type()))
            {
                // convert the value
                final var converter = converter(listener);
                if (converter == null)
                {
                    return new Problem("$: Cannot find type converter", this);
                }
                else
                {
                    final var convertedValue = converter.convert(value);
                    if (convertedValue == null)
                    {
                        return new Problem("$: Cannot convert ${debug} to ${debug}", this, value.getClass(), type());
                    }
                    else
                    {
                        value = convertedValue;
                    }
                }
            }
        }
        else
        {
            if (!isOptional())
            {
                return new Problem("$: Required property was not populated", this);
            }
        }

        if (setter != null)
        {
            return setter.set(object, value);
        }
        else
        {
            return new Problem("$: Setter not found", this);
        }
    }

    public Setter setter()
    {
        return setter;
    }

    public void setter(final Setter setter)
    {
        this.setter = setter;
    }

    @Override
    public String toString()
    {
        return "[Property name = " + name() + ", type = " + type().getSimpleName() + "]";
    }

    public Class<?> type()
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

    @SuppressWarnings("rawtypes")
    Converter converter(final Listener listener)
    {
        final var converter = setter.annotation(KivaKitPropertyConverter.class);
        if (converter != null)
        {
            try
            {
                return converter.value().getConstructor(Listener.class).newInstance(listener);
            }
            catch (final Exception e)
            {
                throw new IllegalStateException("Couldn't construct converter " + converter.value(), e);
            }
        }
        return null;
    }

    private boolean canAssign(final Class<?> from, final Class<?> to)
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

    private boolean isOptional()
    {
        return setter.annotation(KivaKitPropertyOptional.class) != null;
    }
}
