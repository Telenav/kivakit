////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.identifier;

import com.telenav.kivakit.core.kernel.interfaces.collection.LongKeyed;
import com.telenav.kivakit.core.kernel.interfaces.model.Identifiable;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.primitives.Longs;
import com.telenav.kivakit.core.kernel.messaging.Listener;

/**
 * Base class for identifiers
 *
 * @author jonathanl (shibo)
 * @see Quantizable
 * @see Comparable
 * @see Identifiable
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Identifier implements Identifiable, LongKeyed, Comparable<Identifier>
{
    public static class Converter extends Quantizable.Converter<Identifier>
    {
        public Converter(final Listener listener)
        {
            super(listener, identifier -> identifier == null ? null : new Identifier(identifier));
        }
    }

    private final long value;

    public Identifier(final long value)
    {
        this.value = value;
    }

    public long asLong()
    {
        return value;
    }

    public BitCount bitsToRepresent()
    {
        return Longs.bitsToRepresent(value);
    }

    @Override
    public int compareTo(final Identifier that)
    {
        return Long.compare(value, that.value);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Identifier)
        {
            final var that = (Identifier) object;
            return value == that.value;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (int) value;
    }

    @Override
    public long identifier()
    {
        return value;
    }

    @Override
    public long key()
    {
        return value;
    }

    @Override
    public long quantum()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return Long.toString(value);
    }
}
