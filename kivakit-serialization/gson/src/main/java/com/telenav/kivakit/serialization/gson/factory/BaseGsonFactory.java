////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.serialization.gson.factory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapterFactory;
import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.serialization.gson.serializers.StringConverterGsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.version.Version.parseVersion;

/**
 * Factory that produces configured {@link Gson} JSON serializers via {@link #gson()}.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #gson()}</li>
 * </ul>
 *
 * <p><b>Configuration</b></p>
 *
 * <ul>
 *     <li>{@link #addConvertingSerializer(Class, StringConverter)}</li>
 *     <li>{@link #addInstanceCreator(Class, InstanceCreator)}</li>
 *     <li>{@link #addJsonDeserializer(Class, JsonDeserializer)}</li>
 *     <li>{@link #addJsonSerializer(Class, JsonSerializer)}</li>
 *     <li>{@link #addJsonSerializerDeserializer(Class, JsonSerializerDeserializer)}</li>
 *     <li>{@link #addTypeAdapterFactory(TypeAdapterFactory)}</li>
 *     <li>{@link #exclusionStrategy(ExclusionStrategy)}</li>
 *     <li>{@link #ignoreClass(Class)}</li>
 *     <li>{@link #ignoreField(String)}</li>
 * </ul>
 * 
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #dateFormat(String)}</li>
 *     <li>{@link #htmlEscaping(boolean)}</li>
 *     <li>{@link #prettyPrinting(boolean)}</li>
 *     <li>{@link #requireExposeAnnotation(boolean)}</li>
 *     <li>{@link #serializeNulls(boolean)}</li>
 *     <li>{@link #version(Version)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseGsonFactory extends BaseRepeater implements GsonFactory
{
    /**
     * Map from Gson settings to Gson instances
     */
    private static final Map<GsonSettings, Gson> instances = new HashMap<>();

    /**
     * Settings for building a {@link Gson} instance
     */
    private static class GsonSettings
    {
        private final ObjectSet<Class<?>> classesToExclude = ObjectSet.objectSet();

        private String dateFormat;

        private final Map<Class<?>, JsonDeserializer<?>> deserializers = new HashMap<>();

        private boolean escapeHtml;

        private final ObjectSet<ExclusionStrategy> exclusionStrategies = new ObjectSet<>();

        private final ObjectSet<String> fieldsToExclude = ObjectSet.objectSet();

        private final Map<Class<?>, InstanceCreator<?>> instanceCreators = new HashMap<>();

        private boolean prettyPrinting = true;

        private boolean requireExposeAnnotation = false;

        private boolean serializeNulls = false;

        private final Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<>();

        private final List<TypeAdapterFactory> typeAdapterFactories = new ArrayList<>();

        private final Map<Class<?>, Object> typeHierarchyAdapters = new HashMap<>();

        private Version version = parseVersion("1.0");

        public GsonSettings()
        {
            exclusionStrategies.add(new ExclusionStrategy()
            {
                @Override
                public boolean shouldSkipClass(Class<?> type)
                {
                    return classesToExclude.contains(type);
                }

                @Override
                public boolean shouldSkipField(FieldAttributes attributes)
                {
                    return fieldsToExclude.contains(attributes.getName());
                }
            });
        }

        public GsonBuilder builder()
        {
            var builder = new GsonBuilder();

            typeAdapterFactories.forEach(builder::registerTypeAdapterFactory);
            serializers.forEach(builder::registerTypeAdapter);
            deserializers.forEach(builder::registerTypeAdapter);
            instanceCreators.forEach(builder::registerTypeAdapter);
            typeHierarchyAdapters.forEach(builder::registerTypeHierarchyAdapter);

            builder.setVersion(version.asDouble());
            builder.setDateFormat(dateFormat);
            builder.setExclusionStrategies(ObjectList.objectList(exclusionStrategies).asArray(ExclusionStrategy.class));

            if (!escapeHtml)
            {
                builder.disableHtmlEscaping();
            }

            if (requireExposeAnnotation)
            {
                builder.excludeFieldsWithoutExposeAnnotation();
            }

            if (prettyPrinting)
            {
                builder.setPrettyPrinting();
            }

            if (serializeNulls)
            {
                builder.serializeNulls();
            }

            return builder;
        }

        @Override
        public boolean equals(Object object)
        {
            if (object instanceof GsonSettings)
            {
                GsonSettings that = (GsonSettings) object;
                return Objects.areEqualPairs(
                        typeAdapterFactories, that.typeAdapterFactories,
                        serializers, that.serializers,
                        deserializers, that.deserializers,
                        instanceCreators, that.instanceCreators,
                        typeHierarchyAdapters, that.typeHierarchyAdapters,
                        exclusionStrategies, that.exclusionStrategies,
                        dateFormat, that.dateFormat,
                        escapeHtml, that.escapeHtml,
                        requireExposeAnnotation, that.requireExposeAnnotation,
                        prettyPrinting, that.prettyPrinting,
                        serializeNulls, that.serializeNulls,
                        version, that.version);
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return Hash.hashMany(
                    typeAdapterFactories,
                    serializers,
                    deserializers,
                    instanceCreators,
                    typeHierarchyAdapters,
                    exclusionStrategies,
                    dateFormat,
                    escapeHtml,
                    requireExposeAnnotation,
                    prettyPrinting,
                    serializeNulls,
                    version);
        }
    }

    private final GsonSettings settings = new GsonSettings();

    public BaseGsonFactory(Listener listener)
    {
        addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addConvertingSerializer(Class<T> type, StringConverter<T> converter)
    {
        addJsonSerializerDeserializer(type, new StringConverterGsonSerializer<>(converter));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addInstanceCreator(Class<T> type, InstanceCreator<T> creator)
    {
        settings.instanceCreators.put(type, creator);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addJsonDeserializer(Class<T> type, JsonDeserializer<T> deserializer)
    {
        settings.deserializers.put(type, deserializer);
        settings.typeHierarchyAdapters.put(type, deserializer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addJsonSerializer(Class<T> type, JsonSerializer<T> serializer)
    {
        settings.serializers.put(type, serializer);
        settings.typeHierarchyAdapters.put(type, serializer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addJsonSerializerDeserializer(Class<T> type, JsonSerializerDeserializer<T> deserializer)
    {
        settings.serializers.put(type, deserializer);
        settings.deserializers.put(type, deserializer);
        settings.typeHierarchyAdapters.put(type, deserializer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory addTypeAdapterFactory(TypeAdapterFactory factory)
    {
        settings.typeAdapterFactories.add(factory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory dateFormat(String dateFormat)
    {
        settings.dateFormat = dateFormat;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory exclusionStrategy(ExclusionStrategy strategy)
    {
        settings.exclusionStrategies.add(strategy);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Gson gson()
    {
        var instance = instances.get(settings);
        if (instance == null)
        {
            instance = settings.builder().create();
            instances.put(settings, instance);
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory htmlEscaping(boolean escape)
    {
        settings.escapeHtml = escape;
        return this;
    }

    @Override
    public BaseGsonFactory ignoreClass(Class<?> type)
    {
        settings.classesToExclude.add(type);
        return this;
    }

    @Override
    public BaseGsonFactory ignoreField(String name)
    {
        settings.fieldsToExclude.add(name);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory prettyPrinting(boolean prettyPrinting)
    {
        settings.prettyPrinting = prettyPrinting;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory requireExposeAnnotation(boolean require)
    {
        settings.requireExposeAnnotation = require;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory serializeNulls(boolean serializeNulls)
    {
        settings.serializeNulls = serializeNulls;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory version(Version version)
    {
        settings.version = version;
        return this;
    }
}
