package com.telenav.kivakit.resource.converters;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.ResourceIdentifier;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

public class ResourceIdentifierConverter extends BaseStringConverter<ResourceIdentifier>
{
    public ResourceIdentifierConverter(Listener listener)
    {
        super(listener, ResourceIdentifier.class);
    }

    public ResourceIdentifierConverter()
    {
        this(throwingListener());
    }

    @Override
    protected ResourceIdentifier onToValue(String value)
    {
        return new ResourceIdentifier(value);
    }
}
