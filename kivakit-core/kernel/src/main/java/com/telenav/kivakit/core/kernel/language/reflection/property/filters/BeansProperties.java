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

package com.telenav.kivakit.core.kernel.language.reflection.property.filters;

import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Set;

/**
 * A filter using {@link BasePropertyFilter.NamingConvention#BEANS}
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class BeansProperties extends BasePropertyFilter
{
    public static PropertyFilter ALL_PROPERTIES_AND_FIELDS = new BeansProperties(Set.of(
            Include.PUBLIC_PROPERTIES,
            Include.NON_PUBLIC_PROPERTIES,
            Include.TRANSIENT_FIELDS,
            Include.NON_TRANSIENT_FIELDS));

    public static PropertyFilter PUBLIC_PROPERTIES = new BeansProperties(Set.of(Include.PUBLIC_PROPERTIES));

    public static PropertyFilter ALL_PROPERTIES = new BeansProperties(Set.of(Include.PUBLIC_PROPERTIES, Include.NON_PUBLIC_PROPERTIES));

    public static PropertyFilter ALL_PROPERTIES_AND_NON_TRANSIENT_FIELDS = new BeansProperties(Set.of(Include.PUBLIC_PROPERTIES, Include.NON_PUBLIC_PROPERTIES, Include.NON_TRANSIENT_FIELDS));

    public static PropertyFilter INCLUDED_PROPERTIES = new BeansProperties(Set.of(Include.KIVAKIT_INCLUDED_PROPERTIES));

    public static PropertyFilter INCLUDED_PROPERTIES_AND_FIELDS = new BeansProperties(Set.of(Include.KIVAKIT_INCLUDED_PROPERTIES, Include.KIVAKIT_INCLUDED_FIELDS));

    /**
     * @param included Set of fields and properties to include
     */
    public BeansProperties(final Set<Include> included)
    {
        super(NamingConvention.BEANS, included);
    }
}
