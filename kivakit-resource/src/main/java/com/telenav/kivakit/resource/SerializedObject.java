package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.version.VersionedObject;

public class SerializedObject<T> extends VersionedObject<T>
{
    private InstanceIdentifier instance;

    public SerializedObject(VersionedObject<T> object)
    {
        this(object.object(), object.version());
    }

    public SerializedObject(VersionedObject<T> object, InstanceIdentifier instance)
    {
        this(object.object(), object.version(), instance);
    }

    public SerializedObject(T object, Version version, InstanceIdentifier instance)
    {
        super(version, object);
        this.instance = instance;
    }

    public SerializedObject(T object, Version version)
    {
        super(version, object);
    }

    public InstanceIdentifier instance()
    {
        return instance;
    }
}
