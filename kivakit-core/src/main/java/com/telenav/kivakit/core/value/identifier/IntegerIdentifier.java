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

import com.telenav.kivakit.core.internal.lexakai.DiagramIdentifier;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.interfaces.model.Identifiable;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An identifier with an int sized value
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIdentifier.class)
public class IntegerIdentifier implements
        Comparable<IntegerIdentifier>,
        LongValued,
        Identifiable
{
    private int identifier;

    public IntegerIdentifier(int identifier)
    {
        this.identifier = identifier;
    }

    protected IntegerIdentifier()
    {
    }

    @Override
    public int asInt()
    {
        return identifier;
    }

    @Override
    public long asLong()
    {
        return asInt();
    }

    @Override
    public int compareTo(IntegerIdentifier that)
    {
        return Integer.compare(identifier, that.identifier);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof IntegerIdentifier)
        {
            var that = (IntegerIdentifier) object;
            return identifier == that.identifier;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return identifier;
    }

    @IncludeProperty
    @Override
    public long identifier()
    {
        return identifier;
    }

    @Override
    public String toString()
    {
        return Integer.toString(identifier);
    }
}
