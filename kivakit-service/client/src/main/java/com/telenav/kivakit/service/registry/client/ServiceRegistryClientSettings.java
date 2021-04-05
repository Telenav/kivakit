package com.telenav.kivakit.service.registry.client;

import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.network.http.HttpNetworkLocation;

/**
 * @author jonathanl (shibo)
 */
public class ServiceRegistryClientSettings
{
    private Duration accessTimeout;

    private HttpNetworkLocation serverJar;

    @KivaKitPropertyConverter(Duration.Converter.class)
    public ServiceRegistryClientSettings accessTimeout(final Duration timeout)
    {
        accessTimeout = timeout;
        return this;
    }

    @KivaKitIncludeProperty
    public Duration accessTimeout()
    {
        return accessTimeout;
    }

    @KivaKitPropertyConverter(HttpNetworkLocation.Converter.class)
    public ServiceRegistryClientSettings serverJar(final HttpNetworkLocation location)
    {
        serverJar = location;
        return this;
    }

    @KivaKitIncludeProperty
    public HttpNetworkLocation serverJar()
    {
        return serverJar;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
