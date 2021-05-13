////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.reflection.property.filters.method;

import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitProperties;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * A filter that includes all methods with no parameters and a return value other than void as getters and all methods
 * with a void return type and one parameter as setters.
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class AllMethods extends KivaKitProperties
{
    /**
     * @param included Set of fields and properties to include
     */
    public AllMethods(final Set<Include> included)
    {
        super(included);
    }

    @Override
    public boolean includeAsGetter(final Method method)
    {
        return method.getParameterTypes().length == 0 && method.getReturnType() != Void.class;
    }

    @Override
    public boolean includeAsSetter(final Method method)
    {
        return method.getReturnType() == Void.class && method.getParameterTypes().length == 1;
    }

    @Override
    public boolean includeField(final Field field)
    {
        return false;
    }
}
