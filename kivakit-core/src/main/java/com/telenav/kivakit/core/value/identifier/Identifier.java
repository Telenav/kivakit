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

package com.telenav.kivakit.core.value.identifier;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIdentifier;
import com.telenav.kivakit.core.string.KivaKitFormat;
import com.telenav.kivakit.core.value.count.BitCount;
import com.telenav.kivakit.interfaces.model.Identifiable;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Base class for *long* identifiers
 *
 * @author jonathanl (shibo)
 * @see LongValued
 * @see Comparable
 * @see Identifiable
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramIdentifier.class)
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Identifier implements
        Identifiable,
        LongValued,
        Comparable<Identifier>
{
    private final long identifier;

    /**
     * Creates an identifier
     */
    public Identifier(long identifier)
    {
        this.identifier = identifier;
    }

    @Override
    public long asLong()
    {
        return identifier;
    }

    /**
     * Returns the number of bits required to represent this identifier
     */
    public BitCount bitsToRepresent()
    {
        return BitCount.bitsToRepresent(identifier);
    }

    @Override
    public int compareTo(Identifier that)
    {
        return Long.compare(identifier, that.identifier);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Identifier)
        {
            var that = (Identifier) object;
            return identifier == that.identifier;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (int) identifier;
    }

    @KivaKitFormat
    @Override
    public long identifier()
    {
        return identifier;
    }

    @Override
    public long longValue()
    {
        return identifier;
    }

    @Override
    public String toString()
    {
        return Long.toString(identifier);
    }
}
