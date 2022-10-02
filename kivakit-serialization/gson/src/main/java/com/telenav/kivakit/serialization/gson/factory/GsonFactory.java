package com.telenav.kivakit.serialization.gson.factory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.version.Version;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Creates {@link Gson} instances
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface GsonFactory extends Repeater
{
    /**
     * Adds a serializer for the given {@link StringConverter}.
     *
     * @param type The type for the converter
     * @param converter The converter
     */
    <T> GsonFactory addConvertingSerializer(Class<T> type, StringConverter<T> converter);

    /**
     * Adds a {@link Gson} {@link InstanceCreator} to the builder that creates a {@link Gson} when {@link #gson()} is
     * called. The {@link InstanceCreator} is used to create new instances of a type during deserialization.
     */
    <T> GsonFactory addInstanceCreator(Class<T> type, InstanceCreator<T> creator);

    /**
     * Adds a {@link JsonDeserializer} to the builder that creates a {@link Gson} when {@link #gson()} is called. The
     * {@link JsonDeserializer} is used to deserialize fields.
     */
    <T> GsonFactory addJsonDeserializer(Class<T> type, JsonDeserializer<T> deserializer);

    /**
     * Adds a {@link JsonSerializer} to the builder that creates a {@link Gson} when {@link #gson()} is called. The
     * {@link JsonSerializer} is used to serialize fields.
     */
    <T> GsonFactory addJsonSerializer(Class<T> type, JsonSerializer<T> serializer);

    /**
     * Adds a {@link JsonSerializerDeserializer} to the builder that creates a {@link Gson} when {@link #gson()} is
     * called. The A {@link JsonSerializerDeserializer} is a typesafe KivaKit interface for objects that are both a
     * serializer and a deserializer.
     */
    <T> GsonFactory addJsonSerializerDeserializer(Class<T> type, JsonSerializerDeserializer<T> serializerDeserializer);

    GsonFactory addTypeAdapterFactory(TypeAdapterFactory factory);

    GsonFactory dateFormat(String dateFormat);

    GsonFactory exclusionStrategy(ExclusionStrategy strategy);

    /**
     * Supplies a thread-safe {@link Gson} instance by creating or reusing one
     *
     * @return The instance
     */
    Gson gson();

    /**
     * Enables or disables HTML escaping
     */
    GsonFactory htmlEscaping(boolean escape);

    /**
     * Ignores the given class
     */
    GsonFactory ignoreClass(Class<?> type);

    /**
     * Ignores the given field
     */
    GsonFactory ignoreField(String fieldName);

    /**
     * Enables or disables pretty printed JSON output
     */
    GsonFactory prettyPrinting(boolean prettyPrinting);

    /**
     * Requires the @{@link Expose} annotation to serialize fields
     */
    GsonFactory requireExposeAnnotation(boolean require);

    /**
     * Enables or disables serialization of null values
     */
    GsonFactory serializeNulls(boolean serializeNulls);

    /**
     * Sets the version for serializing
     */
    GsonFactory version(Version version);
}
