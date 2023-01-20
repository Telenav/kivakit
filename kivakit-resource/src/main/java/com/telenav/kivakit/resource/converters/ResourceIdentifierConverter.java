package com.telenav.kivakit.resource.converters;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.ResourceIdentifier;

public class ResourceIdentifierConverter extends BaseStringConverter<ResourceIdentifier>
{
    public ResourceIdentifierConverter(Listener listener)
    {
        super(listener, ResourceIdentifier.class);
    }

    @Override
    protected ResourceIdentifier onToValue(String value)
    {
        return new ResourceIdentifier(value);
    }
}
