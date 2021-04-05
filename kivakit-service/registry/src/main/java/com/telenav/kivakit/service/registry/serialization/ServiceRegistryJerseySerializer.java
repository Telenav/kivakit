package com.telenav.kivakit.service.registry.serialization;

import com.telenav.kivakit.core.serialization.jersey.json.JerseyGsonSerializer;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * @author jonathanl (shibo)
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceRegistryJerseySerializer<T> extends JerseyGsonSerializer<T>
{
    public ServiceRegistryJerseySerializer()
    {
        super(new ServiceRegistryGsonFactory());
    }
}
