package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveOutputStream;
import com.telenav.kivakit.resource.writing.WritableResource;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Interface to code that can write {@link SerializableObject}s to a {@link WritableResource}.
 *
 * @author jonathanl (shibo)
 * @see WritableResource
 * @see SerializableObject
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = TESTING_NONE)
public interface ObjectWriter extends
        RepeaterMixin,
        TryTrait
{
    /**
     * Gets the {@link ProgressReporter} to use while writing
     *
     * @return The {@link ProgressReporter}
     */
    ProgressReporter progressReporter();

    /**
     * Writes the given serialized object to the given {@link WritableResource}, including the given metadata in the
     * output.
     *
     * @param resource The output stream
     * @param object The object to write
     * @param metadata The metadata to write
     */
    default <T> void writeObject(@NotNull WritableResource resource,
                                 @NotNull SerializableObject<T> object,
                                 ObjectMetadata... metadata)
    {
        tryCatchThrow(() ->
        {
            var output = new ProgressiveOutputStream(resource.openForWriting(), progressReporter());
            writeObject(output, resource.path(), object, metadata);
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
    <T> void writeObject(@NotNull OutputStream output,
                         @NotNull StringPath path,
                         @NotNull SerializableObject<T> object,
                         @NotNull ObjectMetadata... metadata);

    /**
     * Writes the given serialized object to the given {@link OutputStream}, including the given metadata in the
     * output.
     *
     * @param output The output stream
     * @param path Path associated with the output stream, for diagnostic purposes
     * @param object The object to write
     * @param metadata The metadata to write
     */
    default <T> void writeObject(@NotNull OutputStream output,
                                 @NotNull StringPath path,
                                 @NotNull T object,
                                 @NotNull ObjectMetadata... metadata)
    {
        writeObject(output, path, new SerializableObject<>(object), metadata);
    }
}
