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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telenav.kivakit.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

/**
 * Factory that produces configured {@link Gson} JSON serializers via {@link #newInstance()}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public abstract class GsonFactory implements Factory<Gson>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    private static final ThreadLocal<Boolean> prettyPrinting = ThreadLocal.withInitial(() -> false);

    public static void prettyPrinting(boolean pretty)
    {
        prettyPrinting.set(pretty);
    }

    public <T> void addSerializer(GsonBuilder builder,
                                  Class<T> type,
                                  GsonSerializer<T> serializer)
    {
        builder.registerTypeAdapter(type, serializer);
    }

    public final GsonFactory addSerializers(GsonBuilder builder)
    {
        onAddSerializers(builder);
        return this;
    }

    public GsonBuilder builder()
    {
        var builder = new GsonBuilder();
        if (DEBUG.isDebugOn() || prettyPrinting.get())
        {
            builder.setPrettyPrinting();
        }
        return builder;
    }

    public GsonFactory initialize(GsonBuilder builder)
    {
        onInitialize(builder);
        return this;
    }

    @Override
    public Gson newInstance()
    {
        var builder = builder();
        initialize(builder);
        addSerializers(builder);
        return builder.create();
    }

    @MustBeInvokedByOverriders
    protected void onAddSerializers(GsonBuilder builder)
    {
    }

    @MustBeInvokedByOverriders
    protected void onInitialize(GsonBuilder builder)
    {
    }

    /**
     * @return A GSON serializer for the given string converter
     */
    protected <T> StringConverterGsonSerializer<T> serializer(StringConverter<T> converter)
    {
        return new StringConverterGsonSerializer<>(converter);
    }
}
