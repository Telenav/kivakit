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

package com.telenav.kivakit.core.value.name;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramValue;
import com.telenav.kivakit.interfaces.naming.Nameable;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.interfaces.string.AsString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A name value.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValue.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Name implements
        Named,
        Nameable,
        AsString
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
    public static String nameOf(Object object)
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

    /** The name */
    private String name;

    /**
     * Constructs a name
     */
    public Name(String name)
    {
        this.name = name;
    }

    public Name()
    {
    }

    /**
     * Returns this name in lowercase
     */
    public Name asLowerCaseName()
    {
        return new Name(name.toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString()
    {
        return name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignName(String name)
    {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return name().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return name;
    }
}
