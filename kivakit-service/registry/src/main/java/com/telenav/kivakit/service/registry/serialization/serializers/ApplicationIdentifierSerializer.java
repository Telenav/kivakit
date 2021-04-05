package com.telenav.kivakit.service.registry.serialization.serializers;

import com.telenav.kivakit.core.application.ApplicationIdentifier;
import com.telenav.kivakit.core.serialization.json.PrimitiveGsonSerializer;

/**
 * @author jonathanl (shibo)
 */
public class ApplicationIdentifierSerializer extends PrimitiveGsonSerializer<ApplicationIdentifier, String>
{
    public ApplicationIdentifierSerializer()
    {
        super(String.class);
    }

    @Override
    protected ApplicationIdentifier toObject(final String identifier)
    {
        return new ApplicationIdentifier(identifier);
    }

    @Override
    protected String toPrimitive(final ApplicationIdentifier application)
    {
        return application.identifier();
    }
}
