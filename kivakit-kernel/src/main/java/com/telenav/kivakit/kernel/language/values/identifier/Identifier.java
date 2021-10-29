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

package com.telenav.kivakit.kernel.language.values.identifier;

import com.telenav.kivakit.kernel.interfaces.collection.LongKeyed;
import com.telenav.kivakit.kernel.interfaces.model.Identifiable;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.primitives.Longs;
import com.telenav.kivakit.kernel.language.values.count.BitCount;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for *long* identifiers
 *
 * @author jonathanl (shibo)
 * @see Quantizable
 * @see Comparable
 * @see Identifiable
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
@LexakaiJavadoc(complete = true)
public class Identifier implements Identifiable, LongKeyed, Comparable<Identifier>
{
    /**
     * Converts to and from {@link Identifier}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends Quantizable.Converter<Identifier>
    {
        public Converter(Listener listener)
        {
            super(listener, identifier -> identifier == null ? null : new Identifier(identifier));
        }
    }

    private final long value;

    public Identifier(long value)
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
    public int compareTo(Identifier that)
    {
        return Long.compare(value, that.value);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Identifier)
        {
            var that = (Identifier) object;
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
