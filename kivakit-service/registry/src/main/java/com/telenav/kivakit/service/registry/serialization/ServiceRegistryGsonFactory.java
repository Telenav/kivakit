package com.telenav.kivakit.service.registry.serialization;

import com.google.gson.GsonBuilder;
import com.telenav.kivakit.core.application.ApplicationIdentifier;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.network.core.Port;
import com.telenav.kivakit.core.serialization.json.GsonFactory;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.serialization.serializers.ApplicationIdentifierSerializer;
import com.telenav.kivakit.service.registry.serialization.serializers.ProblemSerializer;
import com.telenav.kivakit.service.registry.serialization.serializers.ServiceTypeSerializer;
import com.telenav.kivakit.service.registry.serialization.serializers.TimeSerializer;

/**
 * @author jonathanl (shibo)
 */
public class ServiceRegistryGsonFactory extends GsonFactory
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Override
    protected GsonBuilder addSerializers(final GsonBuilder builder)
    {
        addSerializer(builder, Port.class, serializer(new Port.Converter(LOGGER)));
        addSerializer(builder, ApplicationIdentifier.class, new ApplicationIdentifierSerializer());
        addSerializer(builder, ServiceType.class, new ServiceTypeSerializer());
        addSerializer(builder, Problem.class, new ProblemSerializer());
        addSerializer(builder, Time.class, new TimeSerializer());
        return builder;
    }
}
