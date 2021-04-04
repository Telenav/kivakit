////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.configuration;

import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.time.Duration;

/**
 * Configuration Java Bean with string conversion methods for each property to allow storage of this configuration in a
 * property file.
 *
 * @author jonathanl (shibo)
 */
public class ServerConfiguration
{
    private int port;

    private Duration timeout;

    @KivaKitIncludeProperty
    public int port()
    {
        return port;
    }

    @KivaKitPropertyConverter(IntegerConverter.class)
    @KivaKitIncludeProperty
    public void port(final int port)
    {
        this.port = port;
    }

    @KivaKitIncludeProperty
    public Duration timeout()
    {
        return timeout;
    }

    @KivaKitPropertyConverter(Duration.Converter.class)
    @KivaKitIncludeProperty
    public void timeout(final Duration timeout)
    {
        this.timeout = timeout;
    }

    @Override
    public String toString()
    {
        return "[Configuration timeout = " + timeout + ", port = " + port + "]";
    }
}
