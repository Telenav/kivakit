package com.telenav.kivakit.service.registry.server.rest;

import com.telenav.kivakit.service.registry.serialization.ServiceRegistryJerseySerializer;
import com.telenav.kivakit.web.jersey.JettyJerseyRestApplication;

import javax.ws.rs.ApplicationPath;

/**
 * @author jonathanl (shibo)
 */
@ApplicationPath("/api")
public class ServiceRegistryRestApplication extends JettyJerseyRestApplication
{
    public ServiceRegistryRestApplication()
    {
        register(new ServiceRegistryRestResource());
        register(new ServiceRegistryJerseySerializer<>());
    }
}
