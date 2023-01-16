package com.telenav.kivakit.serialization.gson.factory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Combines {@link JsonSerializer} and {@link JsonDeserializer} interfaces
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface JsonSerializerDeserializer<T> extends
        JsonSerializer<T>,
        JsonDeserializer<T>
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
}
