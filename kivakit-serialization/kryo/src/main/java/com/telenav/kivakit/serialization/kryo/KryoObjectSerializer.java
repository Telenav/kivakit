package com.telenav.kivakit.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.resource.SerializedObject;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link Kryo} {@link ObjectSerializer} provider.
 *
 * @author jonathanl (shibo)
 */
public class KryoObjectSerializer implements ObjectSerializer
{
    @Override
    public <T> SerializedObject<T> read(final InputStream input, final StringPath path, final Class<T> type,
                                        final ObjectMetadata... metadata)
    {
        return null;
    }

    @Override
    public <T> boolean write(final OutputStream output, final StringPath path, final SerializedObject<T> object,
                             final ObjectMetadata... metadata)
    {
        return false;
    }
}
