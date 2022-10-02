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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramRegistry;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.util.Objects;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * An identifier for a particular instance of a class. Used by {@link Registry} when locating an object by class which
 * has more than one instance.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #singletonInstance()}</li>
 *     <li>{@link #instanceIdentifier(Enum)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Registry
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlExcludeSuperTypes
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class InstanceIdentifier
{
    /** Identifies the one and only instance of a singleton */
    private static final InstanceIdentifier SINGLETON = InstanceIdentifier.instanceIdentifier(Singleton.SINGLETON);

    /** Map from enum name (both simple and fully-qualified) to instance identifier */
    private static final StringMap<InstanceIdentifier> instanceIdentifierForEnumName = new StringMap<>();

    /**
     * Returns an instance identifier for the given enum value
     *
     * @param enumValue The enum value
     * @return The instance identifier
     */
    public static InstanceIdentifier instanceIdentifier(Enum<?> enumValue)
    {
        return new InstanceIdentifier(enumValue);
    }

    /**
     * Returns the {@link InstanceIdentifier} for the given enum value name
     */
    public static InstanceIdentifier instanceIdentifierForEnumName(Listener listener, String enumValueName)
    {
        var identifier = instanceIdentifierForEnumName.get(enumValueName);
        if (identifier == null)
        {
            listener.problem("Invalid instance identifier: $", enumValueName);
        }
        return identifier;
    }

    /**
     * Returns an instance identifier for singleton objects
     */
    public static InstanceIdentifier singletonInstance()
    {
        return SINGLETON;
    }

    public enum Singleton
    {
        SINGLETON
    }

    private final Enum<?> identifier;

    protected InstanceIdentifier(Enum<?> identifier)
    {
        instanceIdentifierForEnumName.put(identifier.name(), this);
        instanceIdentifierForEnumName.put(identifier.getClass().getName() + "." + identifier.name(), this);
        this.identifier = ensureNotNull(identifier, "Instance identifier cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof InstanceIdentifier)
        {
            InstanceIdentifier that = (InstanceIdentifier) object;
            return this.identifier.equals(that.identifier);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash();
    }

    public Enum<?> identifier()
    {
        return identifier;
    }

    RegistryKey key(Class<?> at)
    {
        return new RegistryKey(at.getName() + ":" + identifier.name());
    }
}
