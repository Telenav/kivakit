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
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
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
public class FunctionalGsonFactory extends BaseRepeater implements GsonFactory
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

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

        private boolean requireExposeAnnotation = true;

        private boolean prettyPrinting = true;

        private boolean serializeNulls = false;

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
        }

        public GsonBuilder builder()
        {
            var builder = new GsonBuilder();
            typeAdapterFactories.forEach(builder::registerTypeAdapterFactory);
            serializers.forEach(builder::registerTypeAdapter);
            deserializers.forEach(builder::registerTypeAdapter);
            instanceCreators.forEach(builder::registerTypeAdapter);
            typeHierarchyAdapters.forEach(builder::registerTypeHierarchyAdapter);

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
                        serializeNulls, that.serializeNulls);
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
                    serializeNulls);
        }
    }

    private Settings settings = new Settings();

    public FunctionalGsonFactory(Listener listener)
    {
        addListener(listener);
    }

    public FunctionalGsonFactory(FunctionalGsonFactory that)
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

    public FunctionalGsonFactory withDateFormat(String dateFormat)
    {
        var copy = copy();
        copy.settings.dateFormat = dateFormat;
        return copy;
    }

    public <T> FunctionalGsonFactory withDeserializer(Class<T> type, JsonDeserializer<T> serializer)
    {
        return withAdapter(type, serializer);
    }

    public <T> FunctionalGsonFactory withExclusionStrategy(ExclusionStrategy strategy)
    {
        var copy = copy();
        copy.settings.exclusionStrategies.add(strategy);
        return copy;
    }

    public FunctionalGsonFactory withHtmlEscaping(boolean escape)
    {
        var copy = copy();
        copy.settings.escapeHtml = escape;
        return copy;
    }

    public FunctionalGsonFactory withPrettyPrinting(boolean prettyPrinting)
    {
        var copy = copy();
        copy.settings.prettyPrinting = prettyPrinting;
        return copy;
    }

    public FunctionalGsonFactory withRequireExposeAnnotation(boolean require)
    {
        var copy = copy();
        copy.settings.requireExposeAnnotation = require;
        return copy;
    }

    public <T, Serialization extends JsonSerializer<T> & JsonDeserializer<T>> FunctionalGsonFactory withSerialization(
            Class<T> type, Serialization serialization)
    {
        return withAdapter(type, serialization);
    }

    public FunctionalGsonFactory withSerializeNulls(boolean serializeNulls)
    {
        var copy = copy();
        copy.settings.serializeNulls = serializeNulls;
        return copy;
    }

    public <T> FunctionalGsonFactory withSerializer(Class<T> type, JsonSerializer<? extends T> serializer)
    {
        return withAdapter(type, serializer);
    }

    public FunctionalGsonFactory withTypeAdapterFactory(Class<?> type, TypeAdapterFactory factory)
    {
        return withAdapter(type, factory);
    }

    protected GsonBuilder builder()
    {
        return settings.builder();
    }

    private FunctionalGsonFactory copy()
    {
        return new FunctionalGsonFactory(this);
    }

    private FunctionalGsonFactory withAdapter(Class<?> type, Object object)
    {
        var copy = copy();

        if (object instanceof InstanceCreator<?>)
        {
            copy.settings.instanceCreators.put(type, (InstanceCreator<?>) object);
        }

        if (object instanceof JsonSerializer<?>)
        {
            copy.settings.serializers.put(type, (JsonSerializer<?>) object);
            copy.settings.typeHierarchyAdapters.put(type, object);
        }

        if (object instanceof JsonDeserializer<?>)
        {
            copy.settings.deserializers.put(type, (JsonDeserializer<?>) object);
            copy.settings.typeHierarchyAdapters.put(type, object);
        }

        if (object instanceof TypeAdapterFactory)
        {
            copy.settings.typeAdapterFactories.add((TypeAdapterFactory) object);
        }

        return copy;
    }
}
