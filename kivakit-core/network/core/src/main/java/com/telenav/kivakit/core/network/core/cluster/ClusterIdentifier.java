package com.telenav.kivakit.core.network.core.cluster;

import com.telenav.kivakit.core.kernel.language.values.identifier.StringIdentifier;

/**
 * Identifies a cluster of servers that work together
 *
 * @author jonathanl (shibo)
 */
public class ClusterIdentifier extends StringIdentifier
{
    public ClusterIdentifier(final String identifier)
    {
        super(identifier);
    }

    protected ClusterIdentifier()
    {
    }
}
