package com.telenav.kivakit.service.registry.project;

import com.telenav.kivakit.core.serialization.kryo.KryoTypes;
import com.telenav.kivakit.service.registry.registries.LocalServiceRegistry;
import com.telenav.kivakit.service.registry.registries.NetworkServiceRegistry;

/**
 * @author jonathanl (shibo)
 */
public class ServiceRegistryKryoTypes extends KryoTypes
{
    public ServiceRegistryKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility of serialization, registration groups and the types
        // in each registration group must remain in the same order.
        //----------------------------------------------------------------------------------------------

        group("service-registry", () ->
        {
            register(LocalServiceRegistry.class);
            register(NetworkServiceRegistry.class);
        });
    }
}
