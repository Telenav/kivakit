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

package com.telenav.kivakit.core.registry;

import com.telenav.kivakit.core.lexakai.DiagramRegistry;
import com.telenav.kivakit.core.value.identifier.StringIdentifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * An identifier for a particular instance of a class. Used by {@link Registry} when locating an object by class which
 * has more than one instance.
 *
 * @author jonathanl (shibo)
 * @see Registry
 */
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlExcludeSuperTypes
public class InstanceIdentifier extends StringIdentifier
{
    /** Identifies the one and only instance of a singleton */
    public static final InstanceIdentifier SINGLETON = InstanceIdentifier.of("SINGLETON");

    public static InstanceIdentifier of(Class<?> value)
    {
        return of(value.getSimpleName());
    }

    public static InstanceIdentifier of(Enum<?> value)
    {
        return of(value.name());
    }

    public static InstanceIdentifier of(String value)
    {
        return new InstanceIdentifier(value);
    }

    protected InstanceIdentifier(String string)
    {
        super(ensureNotNull(string, "Instance identifier cannot be null"));
    }

    RegistryKey key(Class<?> at)
    {
        return new RegistryKey(at.getName() + ":" + identifier());
    }
}
