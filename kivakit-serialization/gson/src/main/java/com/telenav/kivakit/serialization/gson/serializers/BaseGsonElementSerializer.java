package com.telenav.kivakit.serialization.gson.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.telenav.kivakit.core.messaging.listeners.ThrowingListenerException;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;

import java.lang.reflect.Type;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Base class for {@link GsonValueSerializer}s that convert from a value type, V, to a {@link JsonElement}. The subclass
 * is responsible for implementing {@link #toJson(Object)} and {@link #toValue(JsonElement)}. If an exception is thrown
 * by the implementation, it will be rethrown as a {@link ThrowingListenerException}.
 *
 * @author Jonathan Locke
 */
public abstract class BaseGsonElementSerializer<V> extends BaseRepeater implements
    GsonValueSerializer<V, JsonElement>
{
    /** The type to serialized */
    private final Class<V> valueType;

    private final ThreadLocal<JsonSerializationContext> serializer = new ThreadLocal<>();

    private final ThreadLocal<JsonDeserializationContext> deserializer = new ThreadLocal<>();

    public BaseGsonElementSerializer(Class<V> valueType)
    {
        this.valueType = ensureNotNull(valueType);

        throwingListener().listenTo(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V deserialize(JsonElement json,
                         Type type,
                         JsonDeserializationContext context) throws JsonParseException
    {
        try
        {
            deserializer.set(context);
            return toValue(json);
        }
        catch (Exception e)
        {
            problem(e, "Deserialization threw exception: $", json);
            return null;
        }
    }

    public JsonDeserializationContext deserializer()
    {
        return deserializer.get();
    }

    public <T> JsonObject serialize(T object)
    {
        return (JsonObject) serializer().serialize(object, object.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(V value,
                                 Type type,
                                 JsonSerializationContext context)
    {
        try
        {
            serializer.set(context);
            return serializer().serialize(toJson(value));
        }
        catch (Exception e)
        {
            problem(e, "Serialization threw exception: $", value);
            return null;
        }
    }

    @Override
    public Class<JsonElement> serializedType()
    {
        return JsonElement.class;
    }

    public JsonSerializationContext serializer()
    {
        return serializer.get();
    }

    @Override
    public Class<V> valueType()
    {
        return valueType;
    }

    /**
     * Deserializes an object property
     *
     * @param element The JSON element
     * @param propertyName The property to deserialize
     * @param type The type of value
     */
    protected <T> T deserialize(JsonElement element,
                                String propertyName,
                                Class<T> type)
    {
        var object = (JsonObject) element;
        if (type.isPrimitive() || type == String.class)
        {
            JsonPrimitive value = object.getAsJsonPrimitive(propertyName);
            return switch (type.getSimpleName())
                {
                    case "Integer" -> type.cast(value.getAsInt());
                    case "Long" -> type.cast(value.getAsLong());
                    case "Boolean" -> type.cast(value.getAsBoolean());
                    case "Float" -> type.cast(value.getAsFloat());
                    case "Double" -> type.cast(value.getAsDouble());
                    case "String" -> type.cast(value.getAsString());
                    default -> unsupported();
                };
        }
        else
        {
            return deserializer().deserialize(object.get(propertyName), type);
        }
    }

    /**
     * Converts a value to its serialized representation
     *
     * @param value The value to serialize
     * @return The serialized representation of the value
     * @throws ThrowingListenerException Thrown if serialization fails
     */
    protected abstract JsonElement toJson(V value);

    /**
     * Converts a serialized object to a value
     *
     * @param object The serialized representation
     * @return The deserialized value
     * @throws ThrowingListenerException Thrown if deserialization fails
     */
    protected abstract V toValue(JsonElement object);
}
