package com.telenav.kivakit.serialization.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapterFactory;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.version.Version;

public interface GsonFactory extends Repeater
{
    Gson gson();

    GsonFactory withDateFormat(String dateFormat);

    <T> GsonFactory withDeserializer(Class<T> type, JsonDeserializer<T> serializer);

    <T> GsonFactory withExclusionStrategy(ExclusionStrategy strategy);

    GsonFactory withHtmlEscaping(boolean escape);

    GsonFactory withPrettyPrinting(boolean prettyPrinting);

    GsonFactory withRequireExposeAnnotation(boolean require);

    <T, Serialization extends JsonSerializer<T> & JsonDeserializer<T>> GsonFactory withSerialization(
            Class<T> type, Serialization serialization);

    GsonFactory withSerializeNulls(boolean serializeNulls);

    <T> GsonFactory withSerializer(Class<T> type, JsonSerializer<? extends T> serializer);

    GsonFactory withTypeAdapterFactory(Class<?> type, TypeAdapterFactory factory);

    GsonFactory withVersion(Version version);
}