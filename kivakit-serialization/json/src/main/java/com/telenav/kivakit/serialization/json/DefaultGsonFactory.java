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

package com.telenav.kivakit.serialization.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapterFactory;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.conversion.core.language.primitive.BooleanConverter;
import com.telenav.kivakit.conversion.core.time.DurationConverter;
import com.telenav.kivakit.conversion.core.time.FrequencyConverter;
import com.telenav.kivakit.conversion.core.value.BytesConverter;
import com.telenav.kivakit.conversion.core.value.ConfidenceConverter;
import com.telenav.kivakit.conversion.core.value.CountConverter;
import com.telenav.kivakit.conversion.core.value.LevelConverter;
import com.telenav.kivakit.conversion.core.value.MaximumConverter;
import com.telenav.kivakit.conversion.core.value.MinimumConverter;
import com.telenav.kivakit.conversion.core.value.PercentConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.kivakit.core.value.level.Confidence;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.core.version.Version;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory that produces configured {@link Gson} JSON serializers via {@link #gson()}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class DefaultGsonFactory extends BaseRepeater implements GsonFactory
{
    private static final Map<Settings, Gson> instances = new HashMap<>();

    private static class Settings
    {
        private List<TypeAdapterFactory> typeAdapterFactories = new ArrayList<>();

        private Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<>();

        private Map<Class<?>, JsonDeserializer<?>> deserializers = new HashMap<>();

        private Map<Class<?>, InstanceCreator<?>> instanceCreators = new HashMap<>();

        private Map<Class<?>, Object> typeHierarchyAdapters = new HashMap<>();

        private ObjectList<ExclusionStrategy> exclusionStrategies = new ObjectList<>();

        private String dateFormat;

        private boolean escapeHtml;

        private boolean requireExposeAnnotation = false;

        private boolean prettyPrinting = true;

        private boolean serializeNulls = false;

        public Version version = Version.of(1, 0);

        public Settings()
        {
        }

        public Settings(Settings that)
        {
            typeAdapterFactories = new ArrayList<>(that.typeAdapterFactories);
            serializers = new HashMap<>(that.serializers);
            deserializers = new HashMap<>(that.deserializers);
            instanceCreators = new HashMap<>(that.instanceCreators);
            typeHierarchyAdapters = new HashMap<>(that.typeHierarchyAdapters);

            exclusionStrategies = new ObjectList<>(that.exclusionStrategies);
            dateFormat = that.dateFormat;
            escapeHtml = that.escapeHtml;
            requireExposeAnnotation = that.requireExposeAnnotation;
            prettyPrinting = that.prettyPrinting;
            serializeNulls = that.serializeNulls;
            version = that.version;
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
            builder.setExclusionStrategies(exclusionStrategies.asArray(ExclusionStrategy.class));

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

        public Settings copy()
        {
            return new Settings(this);
        }

        @Override
        public boolean equals(Object object)
        {
            if (object instanceof Settings)
            {
                Settings that = (Settings) object;
                return Objects.equalPairs(
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
            return Hash.many(
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

    private Settings settings = new Settings();

    public DefaultGsonFactory(Listener listener)
    {
        addListener(listener);

        addSerializer(Boolean.class, new BooleanConverter(listener));
        addSerializer(Bytes.class, new BytesConverter(listener));
        addSerializer(Confidence.class, new ConfidenceConverter(listener));
        addSerializer(Count.class, new CountConverter(listener));
        addSerializer(Duration.class, new DurationConverter(listener));
        addSerializer(Frequency.class, new FrequencyConverter(listener));
        addSerializer(Level.class, new LevelConverter(listener));
        addSerializer(Maximum.class, new MaximumConverter(listener));
        addSerializer(Minimum.class, new MinimumConverter(listener));
        addSerializer(Percent.class, new PercentConverter(listener));
    }

    public DefaultGsonFactory(DefaultGsonFactory that)
    {
        settings = that.settings.copy();
        copyListeners(that);
    }

    @Override
    public synchronized Gson gson()
    {
        var instance = instances.get(settings);
        if (instance == null)
        {
            instance = builder().create();
            instances.put(settings, instance);
        }
        return instance;
    }

    @Override
    public DefaultGsonFactory withDateFormat(String dateFormat)
    {
        var copy = copy();
        copy.settings.dateFormat = dateFormat;
        return copy;
    }

    @Override
    public <T> DefaultGsonFactory withDeserializer(Class<T> type, JsonDeserializer<T> serializer)
    {
        return withAdapter(type, serializer);
    }

    @Override
    public <T> DefaultGsonFactory withExclusionStrategy(ExclusionStrategy strategy)
    {
        var copy = copy();
        copy.settings.exclusionStrategies.add(strategy);
        return copy;
    }

    @Override
    public DefaultGsonFactory withHtmlEscaping(boolean escape)
    {
        var copy = copy();
        copy.settings.escapeHtml = escape;
        return copy;
    }

    @Override
    public DefaultGsonFactory withPrettyPrinting(boolean prettyPrinting)
    {
        var copy = copy();
        copy.settings.prettyPrinting = prettyPrinting;
        return copy;
    }

    @Override
    public DefaultGsonFactory withRequireExposeAnnotation(boolean require)
    {
        var copy = copy();
        copy.settings.requireExposeAnnotation = require;
        return copy;
    }

    @Override
    public <T, Serialization extends JsonSerializer<T> & JsonDeserializer<T>> DefaultGsonFactory withSerialization(
            Class<T> type, Serialization serialization)
    {
        return withAdapter(type, serialization);
    }

    @Override
    public DefaultGsonFactory withSerializeNulls(boolean serializeNulls)
    {
        var copy = copy();
        copy.settings.serializeNulls = serializeNulls;
        return copy;
    }

    @Override
    public <T> DefaultGsonFactory withSerializer(Class<T> type, JsonSerializer<? extends T> serializer)
    {
        return withAdapter(type, serializer);
    }

    @Override
    public DefaultGsonFactory withTypeAdapterFactory(Class<?> type, TypeAdapterFactory factory)
    {
        return withAdapter(type, factory);
    }

    @Override
    public DefaultGsonFactory withVersion(Version version)
    {
        var copy = copy();
        copy.settings.version = version;
        return copy;
    }

    protected GsonBuilder builder()
    {
        return settings.builder();
    }

    private void addAdapter(DefaultGsonFactory factory, Class<?> type, Object object)
    {
        if (object instanceof InstanceCreator<?>)
        {
            factory.settings.instanceCreators.put(type, (InstanceCreator<?>) object);
        }

        if (object instanceof JsonSerializer<?>)
        {
            factory.settings.serializers.put(type, (JsonSerializer<?>) object);
            factory.settings.typeHierarchyAdapters.put(type, object);
        }

        if (object instanceof JsonDeserializer<?>)
        {
            factory.settings.deserializers.put(type, (JsonDeserializer<?>) object);
            factory.settings.typeHierarchyAdapters.put(type, object);
        }

        if (object instanceof TypeAdapterFactory)
        {
            factory.settings.typeAdapterFactories.add((TypeAdapterFactory) object);
        }
    }

    private <T> void addSerializer(Class<T> type, StringConverter<T> converter)
    {
        addAdapter(this, type, new StringConverterGsonSerializer<>(converter));
    }

    private DefaultGsonFactory copy()
    {
        return new DefaultGsonFactory(this);
    }

    private DefaultGsonFactory withAdapter(Class<?> type, Object object)
    {
        var copy = copy();

        addAdapter(copy, type, object);

        return copy;
    }
}
