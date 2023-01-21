package com.telenav.kivakit.serialization.gson.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.telenav.kivakit.core.messaging.listeners.ThrowingListenerException;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Base class for {@link GsonValueSerializer}s that convert from a value type, V, to a {@link JsonElement}. The subclass
 * is responsible for implementing {@link #toJson(JsonSerializationContext, Object)} and
 * {@link #toValue(JsonDeserializationContext, JsonElement)}. If an exception is thrown by the implementation, it will
 * be rethrown as a {@link ThrowingListenerException}.
 *
 * @author Jonathan Locke
 */
public abstract class BaseGsonElementSerializer<V> extends BaseRepeater implements
    GsonValueSerializer<V, JsonElement>
{
    /** The type to serialized */
    private final Class<V> valueType;

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
                          java.lang.reflect.Type typeOfT,
                          JsonDeserializationContext context)
        throws JsonParseException
    {
        try
        {
            return toValue(context, context.deserialize(json, valueType));
        }
        catch (Exception e)
        {
            problem(e, "Deserialization threw exception: $", json);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(V value,
                                  java.lang.reflect.Type typeOfSrc,
                                  JsonSerializationContext context)
    {
        try
        {
            return context.serialize(toJson(context, value), JsonElement.class);
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

    @Override
    public Class<V> valueType()
    {
        return valueType;
    }

    /**
     * Converts a value to its serialized representation
     *
     * @param context The {@link Gson} serialiation context
     * @param value The value to serialize
     * @return The serialized representation of the value
     * @throws ThrowingListenerException Thrown if serialization fails
     */
    protected abstract JsonElement toJson(JsonSerializationContext context, V value);

    /**
     * Converts a serialized object to a value
     *
     * @param context The {@link Gson} deserialization context
     * @param serialized The serialized representation
     * @return The deserialized value
     * @throws ThrowingListenerException Thrown if deserialization fails
     */
    protected abstract V toValue(JsonDeserializationContext context, JsonElement serialized);
}
