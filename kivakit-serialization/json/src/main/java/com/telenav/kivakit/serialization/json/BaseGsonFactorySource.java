package com.telenav.kivakit.serialization.json;

import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;

public abstract class BaseGsonFactorySource extends BaseRepeater implements GsonFactorySource
{
    /**
     * @return A GSON serializer for the given string converter
     */
    protected <T> StringConverterGsonSerializer<T> serializer(StringConverter<T> converter)
    {
        return new StringConverterGsonSerializer<>(converter);
    }
}
