package com.telenav.kivakit.resource.serialization.serializers;

import com.telenav.kivakit.resource.SerializedObject;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;

public interface JsonSerializationProvider
{
    <T> String toJson(SerializedObject<T> object, ObjectMetadata... metadata);

    <T> SerializedObject<T> toObject(String json, ObjectMetadata... metadata);
}
