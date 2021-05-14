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

package com.telenav.kivakit.configuration;

import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.security.Key;

/**
 * <b>Not public API</b>
 * <p>
 * Identifies a configuration by class and {@link InstanceIdentifier}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class)
@UmlNotPublicApi
@UmlExcludeSuperTypes
@LexakaiJavadoc(complete = true)
class ConfigurationIdentifier implements Comparable<Key>
{
    /** The type of configuration */
    @UmlAggregation
    private final Class<?> type;

    /** An instance identifier for use when there is more than one configuration object of the same type */
    @UmlAggregation
    private final InstanceIdentifier instance;

    /**
     * @param type The type of configuration
     */
    public ConfigurationIdentifier(final Class<?> type)
    {
        this.type = type;
        instance = InstanceIdentifier.SINGLETON;
    }

    /**
     * @param type The type of configuration
     * @param instance The instance of the given type
     */
    public ConfigurationIdentifier(final Class<?> type, final InstanceIdentifier instance)
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
        if (object instanceof ConfigurationIdentifier)
        {
            final var that = (ConfigurationIdentifier) object;
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

