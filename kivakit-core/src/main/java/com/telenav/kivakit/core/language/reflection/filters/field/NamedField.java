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

package com.telenav.kivakit.core.language.reflection.filters.field;

import com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Field;

/**
 * This filter matches a field with a particular name in a given naming convention
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public class NamedField extends AllFields
{
    private final String name;

    public NamedField(PropertyNamingConvention convention, String name)
    {
        super(convention);
        this.name = name;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof NamedField)
        {
            var that = (NamedField) object;
            return name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(name);
    }

    @Override
    public boolean includeField(Field field)
    {
        return field.getName().equals(name);
    }
}
