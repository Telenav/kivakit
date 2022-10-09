package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.Arrays;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveInputStream;
import com.telenav.kivakit.resource.Resource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.OBJECT_TYPE;

/**
 * Interface to code that can read {@link SerializableObject}s from a {@link Resource}.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = TESTING_NONE)
public interface ObjectReader extends RepeaterMixin
{
    /**
     * Gets the {@link ProgressReporter} to use while reading
     *
     * @return The {@link ProgressReporter}
     */
    ProgressReporter progressReporter();

    /**
     * Reads an object from the given {@link InputStream}. The type to be read must be in the input, and metadata must
     * specify {@link ObjectMetadata#OBJECT_TYPE}.
     *
     * @param input The input stream
     * @param path The path for the input stream, for diagnostic purposes
     * @param metadata The metadata to read from the input
     */
    default <T> SerializableObject<T> readObject(@NotNull InputStream input,
                                                 @NotNull StringPath path,
                                                 @NotNull ObjectMetadata... metadata)
    {
        ensure(Arrays.contains(metadata, OBJECT_TYPE),
                "Must specify OBJECT_TYPE metadata, or include an explicit type to read");

        return readObject(new ProgressiveInputStream(input, progressReporter()), path, null, metadata);
    }

    /**
     * Reads an object of the given type from the given {@link Resource}.
     *
     * @param resource The resource to read from
     * @param type The type to read (if {@link ObjectMetadata#OBJECT_TYPE} is not supplied
     * @param metadata The metadata to read
     * @return The deserialized object, or null if it couldn't be read
     */
    default <T> SerializableObject<T> readObject(@NotNull Resource resource,
                                                 Class<T> type,
                                                 ObjectMetadata... metadata)
    {
        try (var input = resource.openForReading())
        {
            return readObject(input, resource.path(), type, metadata);
        }
        catch (IOException e)
        {
            problem("Unable to read object from: $", resource);
            return null;
        }
    }

    /**
     * Reads an object from the given {@link Resource}.
     *
     * @param resource The resource to read from
     * @param metadata The metadata to read
     * @return The deserialized object
     */
    default <T> SerializableObject<T> readObject(@NotNull Resource resource,
                                                 ObjectMetadata... metadata)
    {
        return readObject(resource, null, metadata);
    }

    /**
     * Reads an object of the given type from the given {@link InputStream}. If no type is supplied, the type can be
     * read from the stream by supplying {@link ObjectMetadata#OBJECT_TYPE}. It is required to supply a type or
     * {@link ObjectMetadata#OBJECT_TYPE}.
     *
     * @param input The input stream
     * @param path Path associated with the input stream, for diagnostic purposes
     * @param type The type to read (if {@link ObjectMetadata#OBJECT_TYPE} is nor supplied
     * @param metadata The metadata to read
     * @return The deserialized object, or null if it couldn't be read
     */
    <T> SerializableObject<T> readObject(@NotNull InputStream input,
                                         @NotNull StringPath path,
                                         Class<T> type,
                                         @NotNull ObjectMetadata... metadata);
}
