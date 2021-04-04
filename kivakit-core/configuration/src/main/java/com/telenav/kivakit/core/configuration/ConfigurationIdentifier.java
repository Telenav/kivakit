package com.telenav.kivakit.core.configuration;

import com.telenav.kivakit.core.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import com.telenav.kivakit.core.kernel.language.objects.Hash;

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

