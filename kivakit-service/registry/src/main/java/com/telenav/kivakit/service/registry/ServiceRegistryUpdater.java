package com.telenav.kivakit.service.registry;

/**
 * Sends a service registry update from a local service registry to the network service registry.
 *
 * @author jonathanl (shibo)
 */
public interface ServiceRegistryUpdater
{
    /**
     * Sends an update on the given service
     */
    void sendUpdate(Service service);
}

