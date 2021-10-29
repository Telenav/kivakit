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

import com.telenav.kivakit.kernel.interfaces.model.Identifiable;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An identifier with an int sized value
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
@LexakaiJavadoc(complete = true)
public class IntegerIdentifier implements Comparable<IntegerIdentifier>, Identifiable
{
    /**
     * Converts to and from an {@link IntegerIdentifier}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends Quantizable.Converter<IntegerIdentifier>
    {
        public Converter(Listener listener)
        {
            super(listener, identifier -> identifier == null ? null : new IntegerIdentifier(identifier.intValue()));
        }
    }

    private int value;

    public IntegerIdentifier(int value)
    {
        this.value = value;
    }

    protected IntegerIdentifier()
    {
    }

    public int asInt()
    {
        return value;
    }

    public long asLong()
    {
        return asInt();
    }

    @Override
    public int compareTo(IntegerIdentifier that)
    {
        return Integer.compare(value, that.value);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof IntegerIdentifier)
        {
            var that = (IntegerIdentifier) object;
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

    public boolean isGreaterThan(IntegerIdentifier identifier)
    {
        return value > identifier.value;
    }

    public boolean isLessThan(IntegerIdentifier identifier)
    {
        return value < identifier.value;
    }

    @Override
    public String toString()
    {
        return Integer.toString(value);
    }
}
