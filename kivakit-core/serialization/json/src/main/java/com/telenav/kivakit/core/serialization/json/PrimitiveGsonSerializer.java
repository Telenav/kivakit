////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package

        com.telenav.kivakit.core.serialization.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

public abstract class PrimitiveGsonSerializer<T, Primitive> implements GsonSerializer<T>
{
    private final Class<Primitive> type;

    protected PrimitiveGsonSerializer(final Class<Primitive> type)
    {
        this.type = type;
    }

    @Override
    public T deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException
    {
        final Primitive primitive = context.deserialize(json, type);
        return toObject(primitive);
    }

    @Override
    public JsonElement serialize(final T value, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final var primitive = toPrimitive(value);
        return context.serialize(primitive);
    }

    protected abstract T toObject(Primitive scalar);

    protected abstract Primitive toPrimitive(T object);
}
