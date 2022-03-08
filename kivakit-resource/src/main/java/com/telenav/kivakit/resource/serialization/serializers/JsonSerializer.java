package com.telenav.kivakit.resource.serialization.serializers;

import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.SerializedObject;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;

/**
 * Reads and writes a {@link SerializedObject} from/to a <i>.json</i> file using the {@link JsonSerializationProvider}
 * provided by the global {@link Registry}. For implementations of {@link JsonSerializationProvider}, see the
 * <i>kivakit-serialization</i> project.
 *
 * @author jonathanl (shibo)
 * @see ObjectSerializer
 * @see Resource
 * @see Version
 * @see InstanceIdentifier
 */
public class JsonSerializer implements
        ObjectSerializer,
        RegistryTrait
{
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> SerializedObject<T> read(Resource input, Class<T> type, ObjectMetadata... metadata)
    {
        var json = input.asString();
        return require(JsonSerializationProvider.class).toObject(json, metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean write(WritableResource out, SerializedObject<T> object, ObjectMetadata... metadata)
    {
        var json = require(JsonSerializationProvider.class).toJson(object, metadata);
        out.writer().save(json);
        return true;
    }
}
