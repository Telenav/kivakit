package com.telenav.kivakit.serialization.gson.factory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public interface JsonSerializerDeserializer<T> extends
        JsonSerializer<T>,
        JsonDeserializer<T>
{
    default void addToJson(JsonObject json, String name, String value)
    {
        if (value != null)
        {
            json.add(name, new JsonPrimitive(value));
        }
    }

    default void addToJson(JsonObject json, String name, Number value)
    {
        if (value != null)
        {
            json.add(name, new JsonPrimitive(value));
        }
    }

    default <FieldType> FieldType deserializeField(String name,
                                                   JsonObject object,
                                                   JsonDeserializationContext context,
                                                   Type type)
    {
        return context.deserialize(object.get(name), type);
    }
}
