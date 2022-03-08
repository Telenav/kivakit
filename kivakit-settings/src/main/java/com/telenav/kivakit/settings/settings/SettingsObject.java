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

package com.telenav.kivakit.settings.settings;

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeType;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.security.Key;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.SINGLETON;

/**
 * <b>Service Provider API</b>
 *
 * <p>
 * A settings object with a unique type and instance identifier. The {@link Settings} class stores {@link
 * SettingsObject}s in a {@link SettingsStore}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Settings
 * @see SettingsStore
 */
@UmlNotPublicApi
@UmlExcludeType(Comparable.class)
@UmlExcludeType
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
    @UmlNotPublicApi
    @UmlExcludeType(Comparable.class)
    @UmlExcludeType
    public static class Identifier implements Comparable<Key>
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
        public Identifier(Class<?> type)
        {
            this.type = type;
            instance = SINGLETON;
        }

        /**
         * @param type The type of settings object
         * @param instance The instance of the given type
         */
        public Identifier(Class<?> type, InstanceIdentifier instance)
        {
            this.type = type;
            this.instance = instance;
        }

        @Override
        public int compareTo(Key that)
        {
            return toString().compareTo(that.toString());
        }

        @Override
        public boolean equals(Object object)
        {
            if (object instanceof Identifier)
            {
                var that = (Identifier) object;
                return type.equals(that.type) && instance.equals(that.instance);
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return Hash.many(type, instance);
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
    private final Identifier identifier;

    /** The settings object itself */
    private final Object object;

    public SettingsObject(Object object)
    {
        this(object, SINGLETON);
    }

    public SettingsObject(Object object, InstanceIdentifier instance)
    {
        this(object, object.getClass(), instance);
    }

    public SettingsObject(Object object, Class<?> type, InstanceIdentifier instance)
    {
        ensureNotNull(instance);
        ensureNotNull(object);

        this.identifier = new Identifier(type, instance);
        this.object = object;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof SettingsObject)
        {
            SettingsObject that = (SettingsObject) object;
            return this.object == that.object;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.identity(object);
    }

    public Identifier identifier()
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
