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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilterSet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.ANY_NAMING_CONVENTION;

/**
 * A filter that includes all non-static fields
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class AllFields extends PropertyFilterSet
{
    /**
     * Constructs a property filter that matches all fields with the given naming convention
     */
    public AllFields()
    {
        super(ANY_NAMING_CONVENTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeAsGetter(Method method)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeAsSetter(Method method)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeField(Field field)
    {
        return !Modifier.isStatic(field.getModifiers());
    }
}
