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

package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeType;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.security.Key;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * <b>Not public API</b>
 * <p>
 * A settings object with an identifier. Used as a data structure internally.
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlExcludeType(Comparable.class)
@UmlExcludeType
class Entry
{
    /**
     * <b>Not public API</b>
     * <p>
     * Identifies a configuration by class and {@link InstanceIdentifier}.
     *
     * @author jonathanl (shibo)
     */
    @UmlNotPublicApi
    @UmlExcludeType(Comparable.class)
    @UmlExcludeType
    static class Identifier implements Comparable<Key>
    {
        /** The type of settings object */
        @UmlAggregation
        private final Class<?> type;

        /** An instance identifier for use when there is more than one settings object of the same type */
        @UmlAggregation
        private final InstanceIdentifier instance;

        /**
         * @param type The type of configuration
         */
        public Identifier(final Class<?> type)
        {
            this.type = type;
            instance = InstanceIdentifier.SINGLETON;
        }

        /**
         * @param type The type of configuration
         * @param instance The instance of the given type
         */
        public Identifier(final Class<?> type, final InstanceIdentifier instance)
        {
            this.type = type;
            this.instance = instance;
        }

        @Override
        public int compareTo(final Key that)
        {
            return toString().compareTo(that.toString());
        }

        @Override
        public boolean equals(final Object object)
        {
            if (object instanceof Identifier)
            {
                final var that = (Identifier) object;
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
         * @return The instance of this configuration, if any
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
         * @return The type of configuration
         */
        public Class<?> type()
        {
            return type;
        }
    }

    /** Identifier of configuration */
    @UmlAggregation
    private final Identifier identifier;

    /** The configuration object itself */
    private final Object object;

    public Entry(final Identifier identifier, final Object object)
    {
        ensureNotNull(identifier);
        ensureNotNull(object);
        
        this.identifier = identifier;
        this.object = object;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Entry)
        {
            final Entry that = (Entry) object;
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
