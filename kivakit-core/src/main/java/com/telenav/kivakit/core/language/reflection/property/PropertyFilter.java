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

import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.JAVA_BEANS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.KIVAKIT;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.KIVAKIT_AND_JAVA_BEANS;

/**
 * A filter that determines how a {@link Property} field or method can be accessed.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramReflection.class)
public interface PropertyFilter
{
    static PropertyFilter beansProperties(PropertyMembers... included)
    {
        return new PropertyFilterSet(JAVA_BEANS, included);
    }

    static PropertyFilter allProperties(PropertyMembers... included)
    {
        return new PropertyFilterSet(KIVAKIT_AND_JAVA_BEANS, included);
    }

    static PropertyFilter kivakitProperties(PropertyMembers... included)
    {
        return new PropertyFilterSet(KIVAKIT, included);
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
