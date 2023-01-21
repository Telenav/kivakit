package com.telenav.kivakit.serialization.gson.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
     * Adds the given property to the given json object
     *
     * @param json The JSON object
     * @param propertyName The name of the property to add
     * @param propertyValue The value of the property
     */
    default void addToJson(JsonObject json, String propertyName, String propertyValue)
    {
        if (propertyValue != null)
        {
            json.add(propertyName, new JsonPrimitive(propertyValue));
        }
    }

    /**
     * Adds the given property to the given json object
     *
     * @param json The JSON object
     * @param propertyName The name of the property to add
     * @param propertyValue The value of the property
     */
    default void addToJson(JsonObject json, String propertyName, Number propertyValue)
    {
        if (propertyValue != null)
        {
            json.add(propertyName, new JsonPrimitive(propertyValue));
        }
    }

    /**
     * Deserializes a property
     *
     * @param propertyName The property to deserialize
     * @param object The JSON object
     * @param context The deserializer
     * @param type The type of value
     */
    default <PropertyType> PropertyType deserializeProperty(String propertyName,
                                                            JsonObject object,
                                                            JsonDeserializationContext context,
                                                            Class<PropertyType> type)
    {
        return context.deserialize(object.get(propertyName), type);
    }

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
