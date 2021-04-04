////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.identifier;

import com.telenav.kivakit.core.kernel.interfaces.model.Identifiable;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An identifier with an int sized value
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class IntegerIdentifier implements Comparable<IntegerIdentifier>, Identifiable
{
    public static class Converter extends Quantizable.Converter<IntegerIdentifier>
    {
        public Converter(final Listener listener)
        {
            super(listener, identifier -> identifier == null ? null : new IntegerIdentifier(identifier.intValue()));
        }
    }

    private int value;

    public IntegerIdentifier(final int value)
    {
        this.value = value;
    }

    protected IntegerIdentifier()
    {
    }

    public int asInteger()
    {
        return value;
    }

    public long asLong()
    {
        return asInteger();
    }

    @Override
    public int compareTo(final IntegerIdentifier that)
    {
        return Integer.compare(value, that.value);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof IntegerIdentifier)
        {
            final var that = (IntegerIdentifier) object;
            return value == that.value;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return value;
    }

    @KivaKitIncludeProperty
    @Override
    public long identifier()
    {
        return value;
    }

    public boolean isGreaterThan(final IntegerIdentifier identifier)
    {
        return value > identifier.value;
    }

    public boolean isLessThan(final IntegerIdentifier identifier)
    {
        return value < identifier.value;
    }

    @Override
    public String toString()
    {
        return Integer.toString(value);
    }
}
