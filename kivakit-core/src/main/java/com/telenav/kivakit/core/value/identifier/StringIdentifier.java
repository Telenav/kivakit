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
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * An identifier whose value is a {@link String}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIdentifier.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class StringIdentifier implements Comparable<StringIdentifier>
{
    /** The identifier */
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

    @KivaKitFormat
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
