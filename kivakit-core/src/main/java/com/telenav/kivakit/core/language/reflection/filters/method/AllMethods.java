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

package com.telenav.kivakit.core.language.reflection.filters.method;

import com.telenav.kivakit.core.language.reflection.property.PropertyFilterSet;
import com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A filter that includes all methods with no parameters and a return value other than void as getters and all methods
 * with a void return type and one parameter as setters.
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public class AllMethods extends PropertyFilterSet
{
    public AllMethods(PropertyNamingConvention convention)
    {
        super(convention);
    }

    @Override
    public boolean includeAsGetter(Method method)
    {
        return method.getParameterTypes().length == 0 && method.getReturnType() != Void.class;
    }

    @Override
    public boolean includeAsSetter(Method method)
    {
        return method.getReturnType() == Void.class && method.getParameterTypes().length == 1;
    }

    @Override
    public boolean includeField(Field field)
    {
        return false;
    }
}
