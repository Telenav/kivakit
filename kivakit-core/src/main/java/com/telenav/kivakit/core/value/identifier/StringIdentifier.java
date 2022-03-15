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

import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.lexakai.DiagramIdentifier;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An identifier whose value is a {@link String}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIdentifier.class)
@LexakaiJavadoc(complete = true)
public class StringIdentifier implements Comparable<StringIdentifier>
{
    private String identifier;

    public StringIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    protected StringIdentifier()
    {
    }

    public String asString()
    {
        return identifier;
    }

    @Override
    public int compareTo(StringIdentifier that)
    {
        return identifier.compareTo(that.identifier);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof StringIdentifier)
        {
            var that = (StringIdentifier) object;
            return identifier.equals(that.identifier);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return identifier.hashCode();
    }

    @KivaKitIncludeProperty
    public String identifier()
    {
        return identifier;
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
