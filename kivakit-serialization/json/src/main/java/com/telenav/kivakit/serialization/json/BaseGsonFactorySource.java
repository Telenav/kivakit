package com.telenav.kivakit.serialization.json;

import com.telenav.kivakit.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;

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
