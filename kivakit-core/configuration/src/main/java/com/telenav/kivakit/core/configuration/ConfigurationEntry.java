package com.telenav.kivakit.core.configuration;

import com.telenav.kivakit.core.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

/**
 * <b>Not public API</b>
 * <p>
 * A configuration object with an identifier. Used as a data structure internally.
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlClassDiagram(diagram = DiagramConfiguration.class)
class ConfigurationEntry
{
    /** Identifier of configuration */
    @UmlAggregation
    private final ConfigurationIdentifier identifier;

    /** The configuration object itself */
    private final Object object;

    public ConfigurationEntry(final ConfigurationIdentifier identifier, final Object object)
    {
        this.identifier = identifier;
        this.object = object;
    }

    public ConfigurationIdentifier identifier()
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
