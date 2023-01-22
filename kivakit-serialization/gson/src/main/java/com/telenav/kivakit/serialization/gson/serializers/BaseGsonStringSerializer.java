package com.telenav.kivakit.serialization.gson.serializers;

import com.telenav.kivakit.core.messaging.listeners.ThrowingListenerException;

/**
 * Base class for {@link GsonSerializer}s that convert from a value type, V, to a String. The subclass is
 * responsible for implementing {@link #onDeserialize(String)}, and optionally, {@link #onSerialize(Object)} (the
 * default implementation is to call {@link #toString()} on the value. If an exception is * thrown by the
 * implementation, it will be rethrown as a {@link ThrowingListenerException}.
 *
 * @author Jonathan Locke
 */
public abstract class BaseGsonStringSerializer<V> extends BaseGsonSerializer<V, String>
{
    public BaseGsonStringSerializer(Class<V> valueType)
    {
        super(valueType, String.class);
    }

    /**
     * Converts a serialized object to a value
     *
     * @param serialized The serialized representation
     * @return The deserialized value
     */
    @Override
    protected abstract V onDeserialize(String serialized);

    /**
     * Converts a value to its serialized representation
     *
     * @param value The value to serialize
     * @return The serialized representation of the value
     */
    @Override
    protected String onSerialize(V value)
    {
        return value.toString();
    }
}
