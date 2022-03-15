package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveOutputStream;
import com.telenav.kivakit.resource.WritableResource;

import java.io.OutputStream;

/**
 * Interface to code that can write {@link SerializableObject}s to a {@link WritableResource}.
 *
 * @author jonathanl (shibo)
 * @see WritableResource
 * @see SerializableObject
 */
public interface ObjectWriter extends RepeaterMixin, TryTrait
{
    /**
     * Gets the {@link ProgressReporter} to use while writing
     *
     * @return The {@link ProgressReporter}
     */
    ProgressReporter reporter();

    /**
     * Writes the given serialized object to the given {@link WritableResource}, including the given metadata in the
     * output.
     *
     * @param resource The output stream
     * @param object The object to write
     * @param metadata The metadata to write
     */
    default <T> void write(WritableResource resource,
                           SerializableObject<T> object,
                           ObjectMetadata... metadata)
    {
        tryCatchThrow(() ->
        {
            var output = new ProgressiveOutputStream(resource.openForWriting(), reporter());
            write(output, resource.path(), object, metadata);
        }, "Unable to write to $", resource);
    }

    /**
     * Writes the given serialized object to the given {@link OutputStream}, including the given metadata in the
     * output.
     *
     * @param output The output stream
     * @param path Path associated with the output stream, for diagnostic purposes
     * @param object The object to write
     * @param metadata The metadata to write
     */
    <T> void write(OutputStream output,
                   StringPath path,
                   SerializableObject<T> object,
                   ObjectMetadata... metadata);

    /**
     * Writes the given serialized object to the given {@link OutputStream}, including the given metadata in the
     * output.
     *
     * @param output The output stream
     * @param path Path associated with the output stream, for diagnostic purposes
     * @param object The object to write
     * @param metadata The metadata to write
     */
    default <T> void write(OutputStream output,
                           StringPath path,
                           T object,
                           ObjectMetadata... metadata)
    {
        write(output, path, new SerializableObject<>(object), metadata);
    }
}
