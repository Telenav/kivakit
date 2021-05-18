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

package com.telenav.kivakit.kernel.language.reflection.property.filters.method;

import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * This filter matches a field with a particular name
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class NamedMethod extends AllMethods
{
    private final String name;

    public NamedMethod(final String name)
    {
        super(Collections.emptySet());
        this.name = name;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof NamedMethod)
        {
            final var that = (NamedMethod) object;
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
    public boolean includeAsGetter(final Method method)
    {
        return super.includeAsGetter(method) && method.getName().equals(name);
    }
}
