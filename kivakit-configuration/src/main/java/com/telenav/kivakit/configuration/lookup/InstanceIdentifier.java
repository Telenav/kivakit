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

package com.telenav.kivakit.configuration.lookup;

import com.telenav.kivakit.configuration.ConfigurationSet;
import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramLookup;
import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * An identifier for a particular instance of a class of configuration object stored in a {@link ConfigurationSet}. Also
 * used by {@link Registry} when locating an object by class which has more than one instance.
 *
 * @author jonathanl (shibo)
 * @see Registry
 * @see ConfigurationSet
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class)
@UmlClassDiagram(diagram = DiagramLookup.class)
@UmlExcludeSuperTypes
public class InstanceIdentifier extends StringIdentifier
{
    /** Identifies the one and only instance of a singleton */
    public static final InstanceIdentifier SINGLETON = new InstanceIdentifier("[SINGLETON]");

    public InstanceIdentifier(final Class<?> value)
    {
        super(value.getSimpleName());
    }

    public InstanceIdentifier(final Enum<?> value)
    {
        super(value.name());
    }

    public InstanceIdentifier(final String string)
    {
        super(string);
    }
}