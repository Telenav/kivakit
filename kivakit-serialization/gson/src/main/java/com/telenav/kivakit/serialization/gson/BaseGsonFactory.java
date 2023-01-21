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

package com.telenav.kivakit.serialization.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonElementSerializer;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonStringSerializer;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonValueSerializer;
import com.telenav.kivakit.serialization.gson.serializers.GsonValueSerializer;
import com.telenav.kivakit.serialization.gson.serializers.StringConverterGsonSerializer;

import java.util.IdentityHashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;

/**
 * A factory that produces configured {@link Gson} serializers via {@link #gson()}.
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
 *     <li>{@link #addSerializer(GsonValueSerializer)}</li>
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
 * @see BaseGsonValueSerializer
 * @see GsonValueSerializer
 * @see StringConverterGsonSerializer
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public abstract class BaseGsonFactory extends BaseRepeater implements GsonFactory
{
    /**
     * Map from Gson settings to Gson instances
     */
    private static final Map<GsonSettings, Gson> instances = new IdentityHashMap<>();

    /**
     * Interface to code that registers type adapters and otherwise configures a builder
     */
    interface Registrar
    {
        void register(GsonBuilder builder);
    }

    /**
     * Settings for building a {@link Gson} instance
     */
    private static class GsonSettings
    {
        private final ObjectList<Registrar> registrars = list();

        private final ObjectSet<Class<?>> classesToExclude = set();

        private final ObjectSet<String> fieldsToExclude = set();

        private final ObjectSet<String> tokens = set();

        public GsonBuilder builder()
        {
            var builder = new GsonBuilder();
            registrars.forEach(at -> at.register(builder));
            return builder;
        }

        @Override
        public boolean equals(Object object)
        {
            if (object instanceof GsonSettings that)
            {
                return this.tokens.equals(that.tokens);
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return tokens.hashCode();
        }

        public void onBuild(String token, Registrar registrar)
        {
            tokens.add(token);
            registrars.add(registrar);
        }

        void initialize()
        {
            onBuild(token(classesToExclude, fieldsToExclude), builder -> builder.setExclusionStrategies(new ExclusionStrategy()
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
            }));
        }
    }

    private final GsonSettings settings = new GsonSettings();

    private boolean allowReuse = true;

    protected BaseGsonFactory()
    {
        settings.initialize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addGsonDeserializer(Class<T> type, JsonDeserializer<T> deserializer)
    {
        var token = token("json-deserializer", type, deserializer.getClass());

        return onBuild(token, builder ->
        {
            builder.registerTypeAdapter(type, deserializer);
            builder.registerTypeHierarchyAdapter(type, deserializer);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory addGsonExclusionStrategy(ExclusionStrategy strategy)
    {
        var token = token("exclusion-strategy", strategy.getClass());

        return onBuild(token, builder ->
        {
            builder.addDeserializationExclusionStrategy(strategy);
            builder.addSerializationExclusionStrategy(strategy);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addGsonInstanceCreator(Class<T> type, InstanceCreator<T> creator)
    {
        var token = token("instance-creator", type, creator.getClass());

        return onBuild(token, builder -> builder.registerTypeAdapter(type, creator));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addGsonTypeAdapter(Class<T> type, TypeAdapter<T> adapter)
    {
        var token = token("type-adapter", adapter.getClass().getName());

        return onBuild(token, builder -> builder.registerTypeAdapter(type, adapter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory addGsonTypeAdapterFactory(TypeAdapterFactory factory)
    {
        var token = token("type-adapter-factory", factory.getClass().getName());

        return onBuild(token, builder -> builder.registerTypeAdapterFactory(factory));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> BaseGsonFactory addSerializer(Class<T> type, JsonSerializer<T> serializer)
    {
        var token = token("json-serializer", type, serializer.getClass());

        return onBuild(token, builder ->
        {
            builder.registerTypeAdapter(type, serializer);
            builder.registerTypeHierarchyAdapter(type, serializer);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V, S> BaseGsonFactory addSerializer(GsonValueSerializer<V, S> serializer)
    {
        var token = token("value-serializer", serializer.identity());

        return onBuild(token, builder -> builder.registerTypeAdapter(serializer.valueType(), serializer));
    }

    /**
     * Adds a serializer for the given {@link StringConverter}.
     */
    @Override
    public <T> GsonFactory addSerializer(StringConverter<T> converter)
    {
        return addSerializer(new StringConverterGsonSerializer<>(converter));
    }

    @Override
    public GsonFactory allowReuse(boolean allowReuse)
    {
        this.allowReuse = allowReuse;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory dateFormat(String dateFormat)
    {
        var token = token("date-format", dateFormat);

        return onBuild(token, builder -> builder.setDateFormat(dateFormat));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory escapeHtml(boolean enable)
    {
        var token = token("escape-html", enable);

        return enable
            ? this
            : onBuild(token, GsonBuilder::disableHtmlEscaping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Gson gson()
    {
        if (allowReuse)
        {
            var instance = instances.get(settings);
            if (instance == null)
            {
                instance = settings.builder().create();
                instances.put(settings, instance);
            }
            return instance;
        }
        return settings.builder().create();
    }

    @Override
    public BaseGsonFactory ignoreField(String name)
    {
        settings.fieldsToExclude.add(name);
        return this;
    }

    @Override
    public BaseGsonFactory ignoreType(Class<?> type)
    {
        settings.classesToExclude.add(type);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory prettyPrinting(boolean enable)
    {
        var token = token("pretty", enable);

        return enable
            ? onBuild(token, GsonBuilder::setPrettyPrinting)
            : this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory requireExposeAnnotation(boolean require)
    {
        var token = token("require", require);

        return require
            ? onBuild(token, GsonBuilder::excludeFieldsWithoutExposeAnnotation)
            : this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory serializeNulls(boolean enable)
    {
        var token = token("nulls", enable);

        return enable
            ? onBuild(token, GsonBuilder::serializeNulls)
            : this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseGsonFactory version(Version version)
    {
        var token = token("version", version);

        return onBuild(token, builder -> builder.setVersion(version.asDouble()));
    }

    protected BaseGsonFactory onBuild(String token, Registrar registrar)
    {
        settings.onBuild(token, registrar);
        return this;
    }

    private static String token(Object... objects)
    {
        return list(objects).join(":");
    }
}
