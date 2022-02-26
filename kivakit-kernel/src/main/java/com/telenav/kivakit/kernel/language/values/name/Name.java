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

package com.telenav.kivakit.kernel.language.values.name;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.interfaces.naming.Nameable;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.interfaces.string.StringSource;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A name value.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
@LexakaiJavadoc(complete = true)
public class Name implements
        Named,
        Nameable,
        StringSource
{
    /**
     * Extracts a name for the given object by trying the following in order:
     * <ul>
     *     <li>{@link NamedObject#objectName()}</li>
     *     <li>{@link Named#name()}</li>
     *     <li>Create a name from the class of the object</li>
     * </ul>
     *
     * @return A lowercase hyphenated name for the given object
     */
    public static String of(Object object)
    {
        String name = null;
        if (object instanceof NamedObject)
        {
            name = ((NamedObject) object).objectName();
        }
        if (name == null && object instanceof Named)
        {
            name = ((Named) object).name();
        }
        if (name == null)
        {
            name = NamedObject.syntheticName(object);
        }
        return name;
    }

    @JsonProperty
    private String name;

    public Name(String name)
    {
        this.name = name;
    }

    public Name()
    {
    }

    @Override
    public String asString()
    {
        return name();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Name)
        {
            var that = (Name) object;
            return name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return name().hashCode();
    }

    public Name lowerCase()
    {
        return new Name(name.toLowerCase());
    }

    @Override
    @KivaKitIncludeProperty
    public String name()
    {
        return name;
    }

    @Override
    public void name(final String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
