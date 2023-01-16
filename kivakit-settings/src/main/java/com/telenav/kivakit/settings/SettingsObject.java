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

package com.telenav.kivakit.settings;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeType;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.security.Key;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureFalse;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.language.Hash.identityHash;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.instanceIdentifier;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.singletonInstanceIdentifier;

/**
 * <b>Service Provider API</b>
 *
 * <p>
 * A settings object with a unique type and instance identifier. The {@link SettingsRegistry} class stores
 * {@link SettingsObject}s in a {@link SettingsStore}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see SettingsRegistry
 * @see SettingsStore
 */
@UmlNotPublicApi
@UmlExcludeType(Comparable.class)
@UmlExcludeType
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class SettingsObject implements RegistryTrait
{
    /**
     * <b>Service Provider API</b>
     *
     * <p>
     * A compound key that identifies a settings object by {@link Class} and {@link InstanceIdentifier}. The instance
     * identifier is required when there more than one instance of a settings object needs to be stored.
     * </p>
     *
     * @author jonathanl (shibo)
     */
    @SuppressWarnings("unused") @UmlNotPublicApi
    @UmlExcludeType(Comparable.class)
    @UmlExcludeType
    public static class SettingsObjectIdentifier implements Comparable<Key>
    {
        /** The type of settings object */
        @UmlAggregation
        private final Class<?> type;

        /** An instance identifier for use when there is more than one settings object of the same type */
        @UmlAggregation
        private final InstanceIdentifier instance;

        /**
         * @param type The type of settings object
         */
        public SettingsObjectIdentifier(Class<?> type)
        {
            this.type = type;
            instance = singletonInstanceIdentifier();
        }

        /**
         * @param type The type of settings object
         * @param instance The instance of the given type
         */
        public SettingsObjectIdentifier(Class<?> type, InstanceIdentifier instance)
        {
            this.type = type;
            this.instance = instance;
        }

        /**
         * @param type The type of settings object
         * @param instance The instance of the given type
         */
        public SettingsObjectIdentifier(Class<?> type, Enum<?> instance)
        {
            this(type, instanceIdentifier(instance));
        }

        @Override
        public int compareTo(Key that)
        {
            return toString().compareTo(that.toString());
        }

        @Override
        public boolean equals(Object object)
        {
            if (object instanceof SettingsObjectIdentifier that)
            {
                return type.equals(that.type) && instance.equals(that.instance);
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return hashMany(type, instance);
        }

        /**
         * @return The instance of this settings object, if any
         */
        public InstanceIdentifier instance()
        {
            return instance;
        }

        @Override
        public String toString()
        {
            return type.getName() + ":" + instance;
        }

        /**
         * @return The type of settings object
         */
        public Class<?> type()
        {
            return type;
        }
    }

    /** Compound key that uniquely identifies the <i>object</i> */
    @UmlAggregation
    private final SettingsObjectIdentifier identifier;

    /** The settings object itself */
    private final Object object;

    public SettingsObject(Object object)
    {
        this(object, singletonInstanceIdentifier());
    }

    public SettingsObject(SerializableObject<?> object)
    {
        this(object.object(), object.object().getClass(), object.instance());
    }

    public SettingsObject(Object object, InstanceIdentifier instance)
    {
        this(object, object.getClass(), instance);
    }

    public SettingsObject(Object object, Class<?> type, InstanceIdentifier instance)
    {
        if (instance == null)
        {
            instance = singletonInstanceIdentifier();
        }
        ensureNotNull(object);
        ensureFalse(object instanceof SerializableObject, "Internal error: Unwrapped SerializableObject");

        this.identifier = new SettingsObjectIdentifier(type, instance);
        this.object = object;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof SettingsObject that)
        {
            return this.object == that.object;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return identityHash(object);
    }

    public SettingsObjectIdentifier identifier()
    {
        return identifier;
    }

    @SuppressWarnings("unchecked")
    public <T> T object()
    {
        return (T) object;
    }

    @Override
    public String toString()
    {
        return identifier + " = " + object;
    }
}
