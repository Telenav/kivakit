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
import com.telenav.kivakit.core.kernel.data.conversion.string.StringConverter;

import java.lang.reflect.Type;

public class StringConverterGsonSerializer<T> implements GsonSerializer<T>
{
    private final StringConverter<T> converter;

    public StringConverterGsonSerializer(final StringConverter<T> converter)
    {
        this.converter = converter;
    }

    @Override
    public T deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException
    {
        return converter.convert(context.deserialize(json, String.class));
    }

    @Override
    public JsonElement serialize(final T value, final Type typeOfSrc, final JsonSerializationContext context)
    {
        return context.serialize(converter.toString(value));
    }
}
