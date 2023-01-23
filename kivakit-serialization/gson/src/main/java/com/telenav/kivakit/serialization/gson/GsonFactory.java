package com.telenav.kivakit.serialization.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.registry.Register;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonElementSerializer;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonSerializer;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonStringSerializer;
import com.telenav.kivakit.serialization.gson.serializers.GsonSerializer;
import com.telenav.kivakit.serialization.gson.serializers.converter.StringConverterGsonSerializer;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A factory that creates {@link Gson} instances
 *
 * <p><b>Factory</b></p>
 *
 * <ul>
 *     <li>{@link #gson()}</li>
 * </ul>
 *
 * <p><b>Settings</b></p>
 *
 * <ul>
 *     <li>{@link #dateFormat(String)}</li>
 *     <li>{@link #escapeHtml(boolean)}</li>
 *     <li>{@link #prettyPrinting(boolean)}</li>
 *     <li>{@link #requireExposeAnnotation(boolean)}</li>
 *     <li>{@link #serializeNulls(boolean)}</li>
 *     <li>{@link #version(Version)}</li>
 *     <li>{@link #ignoreType(Class)}</li>
 *     <li>{@link #ignoreField(String)}</li>
 * </ul>
 *
 * <p><b>Serializers</b></p>
 *
 * <ul>
 *     <li>{@link #addSerializer(StringConverter)}</li>
 *     <li>{@link #addSerializer(GsonSerializer)}</li>
 * </ul>
 *
 * <p><b>Gson Serializers</b></p>
 *
 * <ul>
 *     <li>{@link #addGsonDeserializer(Class, JsonDeserializer)}</li>
 *     <li>{@link #addGsonExclusionStrategy(ExclusionStrategy)}</li>
 *     <li>{@link #addGsonInstanceCreator(Class, InstanceCreator)}</li>
 *     <li>{@link #addSerializer(Class, JsonSerializer)}</li>
 *     <li>{@link #addGsonTypeAdapter(Class, TypeAdapter)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see GsonFactory
 * @see BaseGsonElementSerializer
 * @see BaseGsonStringSerializer
 * @see BaseGsonSerializer
 * @see GsonSerializer
 * @see StringConverterGsonSerializer
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
@Register
public interface GsonFactory extends Repeater
{
    /**
     * Adds the given {@link Gson} {@link JsonDeserializer} to the {@link Gson} objects produced by this factory
     *
     * @param type The type
     * @param deserializer The deserializer for the given type
     * @return This object, for method chaining
     */
    <T> GsonFactory addGsonDeserializer(Class<T> type, JsonDeserializer<T> deserializer);

    /**
     * Adds the given {@link Gson} {@link ExclusionStrategy} to the {@link Gson} objects produced by this factory
     *
     * @param strategy The exclusion strategy
     * @return This object, for method chaining
     */
    GsonFactory addGsonExclusionStrategy(ExclusionStrategy strategy);

    /**
     * Adds the given {@link Gson} {@link InstanceCreator} to the {@link Gson} objects produced by this factory
     *
     * @param type The type of instances to create
     * @param creator The creator of instances of the given type
     * @return This object, for method chaining
     */
    <T> GsonFactory addGsonInstanceCreator(Class<T> type, InstanceCreator<T> creator);

    /**
     * Adds the given {@link Gson} {@link TypeAdapter} to the {@link Gson} objects produced by this factory
     *
     * @param type The type
     * @param adapter The adapter that provides serialization of the given type
     * @return This object, for method chaining
     */
    <V> GsonFactory addGsonTypeAdapter(Class<V> type, TypeAdapter<V> adapter);

    /**
     * Adds the given {@link Gson} {@link TypeAdapterFactory} to the {@link Gson} objects produced by this factory
     *
     * @param adapter The type adapter factory that provides type adapters for serialization of the given type
     * @return This object, for method chaining
     */
    GsonFactory addGsonTypeAdapterFactory(TypeAdapterFactory adapter);

    /**
     * Adds the given {@link Gson} adapter for the hierarchy of classes with the given base class
     *
     * @param base The base class or interface
     * @param adapter The adapter
     * @return This object, for method chaining
     */
    GsonFactory addGsonTypeHierarchyAdapter(Class<?> base, Object adapter);

    /**
     * Adds the given {@link Gson} {@link JsonSerializer} to the {@link Gson} objects produced by this factory
     *
     * @param type The type
     * @param serializer The serializer for the given type
     * @return This object, for method chaining
     */
    <T> GsonFactory addSerializer(Class<T> type, JsonSerializer<T> serializer);

    /**
     * Adds the given KivaKit {@link GsonSerializer}, which is bidirectional, supporting both serialization and
     * deserialization. Implementations of this interface include subclasses of {@link BaseGsonElementSerializer},
     * {@link BaseGsonSerializer}, and {@link BaseGsonStringSerializer}. Of particular interest is
     * {@link StringConverterGsonSerializer}, which adapts kivakit {@link StringConverter}s as serializers.
     *
     * @param serializer The bidirectional value serializer
     * @return This object, for method chaining
     * @see #addSerializer(StringConverter)
     */
    <V, S> GsonFactory addSerializer(GsonSerializer<V, S> serializer);

    /**
     * Adds the given KivaKit {@link GsonSerializer}, which is bidirectional, supporting both serialization and
     * deserialization. Implementations of this interface include subclasses of {@link BaseGsonElementSerializer},
     * {@link BaseGsonSerializer}, and {@link BaseGsonStringSerializer}. Of particular interest is
     * {@link StringConverterGsonSerializer}, which adapts kivakit {@link StringConverter}s as serializers.
     *
     * @param valueType The type of value to use the serializer for
     * @param serializer The bidirectional value serializer
     * @return This object, for method chaining
     * @see #addSerializer(StringConverter)
     */
    <V, S> BaseGsonFactory addSerializer(Class<V> valueType, GsonSerializer<V, S> serializer);

    /**
     * Adds a serializer for the given {@link StringConverter} to the {@link Gson} objects produced by this factory
     *
     * @param converter The string converter
     * @return This object, for method chaining
     */
    <V> GsonFactory addSerializer(StringConverter<V> converter);

    /**
     * Adds a serializer for the given {@link StringConverter} to the {@link Gson} objects produced by this factory
     *
     * @param valueType The type of value to use the serializer for
     * @param converter The string converter
     * @return This object, for method chaining
     */
    <V> GsonFactory addSerializer(Class<V> valueType, StringConverter<V> converter);

    /**
     * Allows reuse of gson objects with the same configuration (defaults to true)
     *
     * @param allowReuse True to allow reusing
     * @return This object, for method chaining
     */
    GsonFactory allowReuse(boolean allowReuse);

    /**
     * Sets the given date format for {@link Gson} objects produced by this factory
     *
     * @param dateFormat The date format
     * @return This object, for method chaining
     */
    GsonFactory dateFormat(String dateFormat);

    /**
     * Enables or disables HTML escaping for {@link Gson} objects produced by this factory
     *
     * @param enable True to enable HTML escaping
     * @return This object, for method chaining
     */
    GsonFactory escapeHtml(boolean enable);

    /**
     * Factory method that creates a thread-safe {@link Gson} instance by either creating or reusing one with the same
     * configuration
     *
     * @return The instance
     */
    Gson gson();

    /**
     * Ignores the given field for {@link Gson} objects produced by this factory
     *
     * @param fieldName The name of the field to ignore
     * @return This object, for method chaining
     */
    GsonFactory ignoreField(String fieldName);

    /**
     * Ignores the given class for {@link Gson} objects produced by this factory
     *
     * @param type The class to ignore
     * @return This object, for method chaining
     */
    GsonFactory ignoreType(Class<?> type);

    /**
     * Enables or disables JSON pretty-printing for {@link Gson} objects produced by this factory
     *
     * @param enable True to enable HTML escaping
     * @return This object, for method chaining
     */
    GsonFactory prettyPrinting(boolean enable);

    /**
     * Enables or disables the {@link Expose} annotation requirement for {@link Gson} objects produced by this factory
     *
     * @param require True to require {@link Expose} annotations on fields
     * @return This object, for method chaining
     */
    GsonFactory requireExposeAnnotation(boolean require);

    /**
     * Enables or disables null serialization for {@link Gson} objects produced by this factory
     *
     * @param enable True to enable null serialization
     * @return This object, for method chaining
     */
    GsonFactory serializeNulls(boolean enable);

    /**
     * Sets the version for {@link Gson} objects produced by this factory
     *
     * @param version The version
     * @return This object, for method chaining
     */
    GsonFactory version(Version version);
}
