////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package

        com.telenav.kivakit.core.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telenav.kivakit.core.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;

public abstract class GsonFactory implements Factory<Gson>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    private static final ThreadLocal<Boolean> prettyPrinting = ThreadLocal.withInitial(() -> false);

    public static void prettyPrinting(final boolean pretty)
    {
        prettyPrinting.set(pretty);
    }

    @Override
    public Gson newInstance()
    {
        final var builder = addSerializers(new GsonBuilder());
        builder.serializeNulls();
        if (DEBUG.isDebugOn() || prettyPrinting.get())
        {
            builder.setPrettyPrinting();
        }
        return builder.create();
    }

    protected <T> void addSerializer(final GsonBuilder builder, final Class<T> type,
                                     final GsonSerializer<T> serializer)
    {
        builder.registerTypeAdapter(type, serializer);
    }

    protected abstract GsonBuilder addSerializers(GsonBuilder builder);

    protected <T> StringConverterGsonSerializer<T> serializer(final StringConverter<T> converter)
    {
        return new StringConverterGsonSerializer<>(converter);
    }
}
