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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.ANY_NAMING_CONVENTION;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.JAVA_BEANS_NAMING;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.KIVAKIT_PROPERTY_NAMING;

/**
 * A filter that determines how a {@link Property} field or method can be accessed.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramReflection.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface PropertyFilter
{
    /**
     * Returns the specified members as a property filter, which selects only these members. Both Java Beans and KivaKit
     * style properties are included.
     */
    static PropertyFilter allProperties(PropertyMemberSelector... included)
    {
        return new PropertySet(ANY_NAMING_CONVENTION, included);
    }

    /**
     * Returns the specified members as a property filter, which selects only these members. Only JavaBeans properties
     * are included.
     */
    static PropertyFilter beansProperties(PropertyMemberSelector... included)
    {
        return new PropertySet(JAVA_BEANS_NAMING, included);
    }

    /**
     * Returns the specified members as a property filter, which selects only these members. Only KivaKit style
     * properties are included.
     */
    static PropertyFilter kivakitProperties(PropertyMemberSelector... included)
    {
        return new PropertySet(KIVAKIT_PROPERTY_NAMING, included);
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
