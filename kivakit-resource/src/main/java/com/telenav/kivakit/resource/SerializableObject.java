package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.version.VersionedObject;

public class SerializableObject<T> extends VersionedObject<T>
{
    private InstanceIdentifier instance;

    public SerializableObject(VersionedObject<T> object)
    {
        this(object.object(), object.version());
    }

    public SerializableObject(VersionedObject<T> object, InstanceIdentifier instance)
    {
        this(object.object(), object.version(), instance);
    }

    public SerializableObject(T object, Version version, InstanceIdentifier instance)
    {
        super(object, version);
        this.instance = instance;
    }

    public SerializableObject(T object, Version version)
    {
        super(object, version);
    }

    public SerializableObject(T object)
    {
        this(object, null);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (super.equals(object))
        {
            if (object instanceof SerializableObject)
            {
                var that = (SerializableObject<?>) object;
                return Objects.equalPairs(this.instance, that.instance);
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(super.hashCode(), this.instance);
    }

    public InstanceIdentifier instance()
    {
        return instance;
    }
}
