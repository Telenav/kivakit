package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveOutputStream;
import com.telenav.kivakit.resource.SerializedObject;
import com.telenav.kivakit.resource.WritableResource;

import java.io.OutputStream;

/**
 * Interface to code that can write {@link SerializedObject}s to a {@link WritableResource}.
 *
 * @author jonathanl (shibo)
 * @see WritableResource
 * @see SerializedObject
 */
public interface ObjectWriter extends RepeaterMixin
{
    /**
     * Gets the {@link ProgressReporter} to use while writing
     *
     * @return The {@link ProgressReporter}
     */
    ProgressReporter reporter();

    /**
     * Writes the given serialized object to the given {@link WritableResource}, including the given metadata in the
     * output. The type will always be written before the version. Any instance identifier will always be written last.
     *
     * @param resource The output stream
     * @param object The object to write
     * @param metadata The metadata to write
     * @return True if the object was successfully written
     */
    default <T> boolean write(WritableResource resource,
                              SerializedObject<T> object,
                              ObjectMetadata... metadata)
    {
        var output = new ProgressiveOutputStream(resource.openForWriting(), reporter());
        return write(output, resource.path(), object, metadata);
    }

    /**
     * Writes the given serialized object to the given {@link OutputStream}, including the given metadata in the output.
     * The type will always be written before the version. Any instance identifier will always be written last.
     *
     * @param output The output stream
     * @param path Path associated with the output stream, for diagnostic purposes
     * @param object The object to write
     * @param metadata The metadata to write
     * @return True if the object was successfully written
     */
    <T> boolean write(OutputStream output,
                      StringPath path,
                      SerializedObject<T> object,
                      ObjectMetadata... metadata);
}
