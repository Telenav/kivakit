package com.telenav.kivakit.serialization.gson.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.telenav.kivakit.core.messaging.listeners.ThrowingListenerException;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;

import java.lang.reflect.Type;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Base class for {@link GsonSerializer}s that convert from a value type, V, to a serialized type, S. The subclass is
 * responsible for implementing {@link #onSerialize(Object)} and {@link #onDeserialize(Object)}. If an exception is
 * thrown by the implementation, it will be rethrown as a {@link ThrowingListenerException}.
 *
 * @author Jonathan Locke
 */
public abstract class BaseGsonSerializer<V, S> extends BaseRepeater implements
    GsonSerializer<V, S>
{
    /** The type to serialized */
    private final Class<V> valueType;

    /** The type of the serialized representation */
    private final Class<S> serializedType;

    public BaseGsonSerializer(Class<V> valueType,
                              Class<S> serializedType)
    {
        this.valueType = ensureNotNull(valueType);
        this.serializedType = ensureNotNull(serializedType);

        throwingListener().listenTo(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V deserialize(JsonElement json,
                         Type type,
                         JsonDeserializationContext context)
        throws JsonParseException
    {
        try
        {
            // Deserialize the JSON into the serialized type, then convert it
            // to a value by calling onDeserialize
            return onDeserialize(context.deserialize(json, serializedType()));
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
    public JsonElement serialize(V value, Type type, JsonSerializationContext context)
    {
        try
        {
            // Convert the value to the serialized type and then turn the
            // serialized value into JSON
            return context.serialize(onSerialize(value), serializedType());
        }
        catch (Exception e)
        {
            problem(e, "Serialization threw exception: $", value);
            return null;
        }
    }

    @Override
    public Class<S> serializedType()
    {
        return serializedType;
    }

    @Override
    public Class<V> valueType()
    {
        return valueType;
    }

    /**
     * Converts a serialized object to a value
     *
     * @param serialized The serialized representation
     * @return The deserialized value
     * @throws ThrowingListenerException Thrown if deserialization fails
     */
    protected abstract V onDeserialize(S serialized);

    /**
     * Converts a value to its serialized representation
     *
     * @param value The value to serialize
     * @return The serialized representation of the value
     * @throws ThrowingListenerException Thrown if serialization fails
     */
    protected abstract S onSerialize(V value);
}
