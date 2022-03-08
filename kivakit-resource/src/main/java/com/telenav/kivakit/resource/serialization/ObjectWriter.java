package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.resource.SerializedObject;
import com.telenav.kivakit.resource.WritableResource;

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
     * Writes the given serialized object to the given {@link WritableResource}, including the given metadata in the
     * output. The type will always be written before the version. Any instance identifier will always be written last.
     *
     * @param out The output stream
     * @param object The object to write
     * @param metadata The metadata to write
     * @return True if the object was successfully written
     */
    <T> boolean write(WritableResource out, SerializedObject<T> object, ObjectMetadata... metadata);
}
