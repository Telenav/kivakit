package com.telenav.kivakit.service.registry.serialization.serializers;

import com.telenav.kivakit.core.serialization.json.PrimitiveGsonSerializer;
import com.telenav.kivakit.service.registry.ServiceType;

/**
 * @author jonathanl (shibo)
 */
public class ServiceTypeSerializer extends PrimitiveGsonSerializer<ServiceType, String>
{
    public ServiceTypeSerializer()
    {
        super(String.class);
    }

    @Override
    protected ServiceType toObject(final String identifier)
    {
        return new ServiceType(identifier);
    }

    @Override
    protected String toPrimitive(final ServiceType type)
    {
        return type.identifier();
    }
}
