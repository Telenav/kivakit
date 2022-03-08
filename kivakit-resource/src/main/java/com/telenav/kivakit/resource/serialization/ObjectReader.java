package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.core.language.Arrays;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.SerializedObject;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Interface to code that can read {@link SerializedObject}s from a {@link Resource}.
 *
 * @author jonathanl (shibo)
 */
public interface ObjectReader extends RepeaterMixin
{
    /**
     * Reads an object of the given type from the given {@link Resource}. If no type is supplied, the type can be read
     * from the stream by supplying {@link ObjectMetadata#TYPE}. It is required to supply a type or {@link
     * ObjectMetadata#TYPE}. The type will always be read before any version. Any instance identifier will always be
     * read last.
     *
     * @param input The input stream
     * @param type The type to read (if {@link ObjectMetadata#TYPE} is nor supplied
     * @param metadata The metadata to read
     * @return The deserialized object
     */
    <T> SerializedObject<T> read(Resource input, Class<T> type, ObjectMetadata... metadata);

    /**
     * Reads an object from the given {@link Resource}. The type must be read by supplying {@link ObjectMetadata#TYPE}.
     *
     * @param input The input stream
     * @param metadata The metadata to read
     * @return The deserialized object
     */
    default <T> SerializedObject<T> read(Resource input, ObjectMetadata... metadata)
    {
        ensure(Arrays.contains(metadata, ObjectMetadata.TYPE), "Must specify Metadata.TYPE");
        return read(input, null, metadata);
    }
}
