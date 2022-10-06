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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention;
import com.telenav.kivakit.core.language.reflection.property.PropertySet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A filter that includes all methods with the given naming convention.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class AllMethods extends PropertySet
{
    public AllMethods(PropertyNamingConvention convention)
    {
        super(convention);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeAsGetter(Method method)
    {
        return method.parameterTypes().length == 0 && method.returnType() != Void.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeAsSetter(Method method)
    {
        return method.returnType() == Void.class && method.parameterTypes().length == 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeField(Field field)
    {
        return false;
    }
}
