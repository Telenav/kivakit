package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.version.VersionedObject;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.language.Objects.areEqualPairs;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.singleton;

/**
 * A serializable object is a versioned object with an optional instance identifier
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = UNSTABLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public class SerializableObject<T> extends VersionedObject<T>
{
    /** The instance identifier for this serializable object */
    private InstanceIdentifier instance = singleton();

    public SerializableObject(@NotNull VersionedObject<T> object)
    {
        this(object.object(), object.version());
    }

    public SerializableObject(@NotNull VersionedObject<T> object,
                              @NotNull InstanceIdentifier instance)
    {
        this(object.object(), object.version(), instance);
    }

    public SerializableObject(@NotNull T object,
                              Version version,
                              @NotNull InstanceIdentifier instance)
    {
        super(object, version);
        this.instance = instance;
    }

    public SerializableObject(@NotNull T object,
                              Version version)
    {
        super(object, version);
    }

    public SerializableObject(@NotNull T object)
    {
        super(object, null);
    }

    @Override
    public boolean equals(Object object)
    {
        if (super.equals(object))
        {
            if (object instanceof SerializableObject<?> that)
            {
                return areEqualPairs(this.instance, that.instance);
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashMany(super.hashCode(), this.instance);
    }

    public InstanceIdentifier instance()
    {
        return instance;
    }
}
