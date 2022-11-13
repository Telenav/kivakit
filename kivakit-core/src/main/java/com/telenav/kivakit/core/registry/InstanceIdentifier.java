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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramRegistry;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.language.Objects.areEqualPairs;

/**
 * An identifier for a particular instance of a class. Used by {@link Registry} when locating an object by class which
 * has more than one instance.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #singletonInstanceIdentifier()}</li>
 *     <li>{@link #instanceIdentifier(Enum)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Registry
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlExcludeSuperTypes
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class InstanceIdentifier
{
    /** Identifies the one and only instance of a singleton */
    private static InstanceIdentifier SINGLETON;

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
     * Returns an instance identifier for the given string
     *
     * @param string The string value
     * @return The instance identifier
     */
    public static InstanceIdentifier instanceIdentifier(String string)
    {
        return new InstanceIdentifier(string);
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
    public static InstanceIdentifier singletonInstanceIdentifier()
    {
        if (SINGLETON == null)
        {
            SINGLETON = new InstanceIdentifier("SINGLETON");
        }
        return SINGLETON;
    }

    /** Any enum value */
    private final Enum<?> enumIdentifier;

    /** Any string identifier */
    private final String stringIdentifier;

    /**
     * Create an instance identifier for the given enum
     *
     * @param enumValue The enum value
     */
    protected InstanceIdentifier(Enum<?> enumValue)
    {
        instanceIdentifierForEnumName.put(enumValue.name(), this);
        instanceIdentifierForEnumName.put(enumValue.getClass().getName() + "." + enumValue.name(), this);
        this.enumIdentifier = ensureNotNull(enumValue, "Instance identifier cannot be null");
        this.stringIdentifier = null;
    }

    /**
     * Create an instance identifier for the given string
     *
     * @param string The string value
     */
    protected InstanceIdentifier(String string)
    {
        this.enumIdentifier = null;
        this.stringIdentifier = ensureNotNull(string);
    }

    /**
     * Returns any enum identifier
     */
    public Enum<?> enumIdentifier()
    {
        return enumIdentifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof InstanceIdentifier that)
        {
            return areEqualPairs(
                    this.enumIdentifier, that.enumIdentifier,
                    this.stringIdentifier, that.stringIdentifier);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return hashMany(enumIdentifier, stringIdentifier);
    }

    /**
     * Return the name of this identifier
     */
    public String name()
    {
        return enumIdentifier != null ? enumIdentifier.name() : stringIdentifier;
    }

    /**
     * Returns any string identifier
     */
    public String stringIdentifier()
    {
        return stringIdentifier;
    }

    @Override
    public String toString()
    {
        return name();
    }

    RegistryKey key(Class<?> at)
    {
        return new RegistryKey(at.getName() + ":" + identifier());
    }

    private String identifier()
    {
        return enumIdentifier != null ? enumIdentifier.name() : stringIdentifier;
    }
}
