package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.core.language.Arrays;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.resource.Resource;

import java.io.InputStream;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Interface to code that can read {@link SerializableObject}s from a {@link Resource}.
 *
 * @author jonathanl (shibo)
 */
public interface ObjectReader extends RepeaterMixin
{
    /**
     * Reads an object of the given type from the given {@link InputStream}. If no type is supplied, the type can be
     * read from the stream by supplying {@link ObjectMetadata#TYPE}. It is required to supply a type or {@link
     * ObjectMetadata#TYPE}.
     *
     * @param input The input stream
     * @param path Path associated with the input stream, for diagnostic purposes
     * @param type The type to read (if {@link ObjectMetadata#TYPE} is nor supplied
     * @param metadata The metadata to read
     * @return The deserialized object
     */
    <T> SerializableObject<T> read(InputStream input,
                                   StringPath path,
                                   Class<T> type,
                                   ObjectMetadata... metadata);

    /**
     * Reads an object from the given {@link InputStream}. The type to be read must be in the input, and metadata must
     * specify {@link ObjectMetadata#TYPE}.
     *
     * @param input The input stream
     * @param path The path for the input stream, for diagnostic purposes
     * @param metadata The metadata to read from the input
     */
    default <T> SerializableObject<T> read(InputStream input,
                                           StringPath path,
                                           ObjectMetadata... metadata)
    {
        ensure(Arrays.contains(metadata, ObjectMetadata.TYPE),
                "Must specify ObjectMetadata.TYPE, or include an explicit type to read");

        return read(input, path, null, metadata);
    }

    /**
     * Reads an object of the given type from the given {@link Resource}.
     *
     * @param resource The resource to read from
     * @param type The type to read (if {@link ObjectMetadata#TYPE} is not supplied
     * @param metadata The metadata to read
     * @return The deserialized object
     */
    default <T> SerializableObject<T> read(Resource resource,
                                           Class<T> type,
                                           ObjectMetadata... metadata)
    {
        var input = resource.openForReading(); // reporter().progressiveInput(resource.openForReading());
        return read(input, resource.path(), type, metadata);
    }

    /**
     * Reads an object from the given {@link Resource}.
     *
     * @param resource The resource to read from
     * @param metadata The metadata to read
     * @return The deserialized object
     */
    default <T> SerializableObject<T> read(Resource resource,
                                           ObjectMetadata... metadata)
    {
        return read(resource, null, metadata);
    }

    /**
     * Gets the {@link ProgressReporter} to use while reading
     *
     * @return The {@link ProgressReporter}
     */
    ProgressReporter reporter();
}
