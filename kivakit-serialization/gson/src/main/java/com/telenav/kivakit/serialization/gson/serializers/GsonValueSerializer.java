package com.telenav.kivakit.serialization.gson.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Repeater;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Combines {@link Gson}'s {@link JsonSerializer} and {@link JsonDeserializer} interfaces into one
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface GsonValueSerializer<V, S> extends
    JsonSerializer<V>,
    JsonDeserializer<V>,
    Repeater
{
    /**
     * Returns a string that identifies this serializer
     */
    default String identity()
    {
        return getClass() + ":" + valueType() + ":" + serializedType();
    }

    /**
     * Returns the serialized type for this value serializer
     */
    Class<S> serializedType();

    /**
     * Returns the value type for this value serializer
     */
    Class<V> valueType();
}
