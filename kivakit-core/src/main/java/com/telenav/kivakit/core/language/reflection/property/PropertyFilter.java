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

package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.core.language.reflection.property.filters.PropertyFilterSet;
import com.telenav.kivakit.core.project.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A filter that determines how a {@link Property} field or method can be accessed.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public interface PropertyFilter
{
    static PropertyFilter beansProperties(IncludeProperty... included)
    {
        return new PropertyFilterSet(NamingConvention.JAVA_BEANS, included);
    }

    static PropertyFilter kivakitProperties(IncludeProperty... included)
    {
        return new PropertyFilterSet(NamingConvention.KIVAKIT, included);
    }

    /** True if the given method should be included as a property getter */
    boolean includeAsGetter(Method method);

    /** True if the given method should be included as a property setter */
    boolean includeAsSetter(Method method);

    /** True if the given field should be included */
    boolean includeField(Field field);

    /** The name of the field */
    String nameForField(Field field);

    /** A non-camelcase name of the method */
    String nameForMethod(Method method);
}
