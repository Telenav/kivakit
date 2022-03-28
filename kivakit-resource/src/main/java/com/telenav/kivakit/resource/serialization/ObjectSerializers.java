package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.resource.Extension;

import java.util.HashMap;
import java.util.Map;

/**
 * A map from {@link Extension} to {@link ObjectSerializer}. By default, serializers are available for
 * <i>.properties</i> and <i>.json</i> files.
 *
 * @author jonathanl (shibo)
 */
public class ObjectSerializers
{
    private final Map<Extension, ObjectSerializer> serializers = new HashMap<>();

    public ObjectSerializers()
    {
    }

    public ObjectSerializers add(Extension extension, ObjectSerializer serializer)
    {
        serializers.put(extension, serializer);
        return this;
    }

    public ObjectSerializer serializer(Extension extension)
    {
        return serializers.get(extension);
    }
}
