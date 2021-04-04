////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseListConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.network.core.project.lexakai.diagrams.DiagramPort;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import static com.telenav.kivakit.core.network.core.Protocol.HTTP;
import static com.telenav.kivakit.core.network.core.Protocol.HTTPS;

/**
 * A host, port and protocol.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A unique port on the network that communicates with clients using a specific protocol")
@UmlClassDiagram(diagram = DiagramPort.class)
public class Port
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static Port from(final URI uri)
    {
        final var host = new Host(uri.getHost());
        final var scheme = uri.getScheme();
        final var port = uri.getPort();
        final var protocol = Protocol.forName(scheme);
        return new Port(host, protocol, port);
    }

    public static Port parse(final String port)
    {
        return new Converter(LOGGER).convert(port);
    }

    public static class Converter extends BaseStringConverter<Port>
    {
        private final Host.Converter hostConverter;

        private final IntegerConverter integerConverter;

        public Converter(final Listener listener)
        {
            super(listener);
            integerConverter = new IntegerConverter(listener);
            hostConverter = new Host.Converter(listener);
        }

        @Override
        protected Port onConvertToObject(final String value)
        {
            // It is expected that the port is in the format <host>:<portNumber>. If
            // no <portNumber> is specified then 80 is assumed.
            final var parts = value.split(":");
            final var host = hostConverter.convert(parts[0]);
            var portNumber = 80;
            if (parts.length > 1)
            {
                final var portInteger = integerConverter.convert(parts[1]);
                if (portInteger != null)
                {
                    portNumber = portInteger;
                }
            }
            return new Port(host, portNumber);
        }
    }

    public static class ListConverter extends BaseListConverter<Port>
    {
        public ListConverter(final Listener listener)
        {
            super(listener, new Converter(listener), ",");
        }

        public ListConverter(final Listener listener, final String delimiter)
        {
            super(listener, new Converter(listener), delimiter);
        }
    }

    @JsonProperty
    @Schema(description = "The host which owns the port",
            required = true)
    @UmlAggregation
    private Host host;

    @JsonProperty
    @Schema(description = "The port number",
            example = "8080",
            required = true)
    private int port;

    @JsonProperty
    @Schema(description = "The protocol that is used to communicate with the port",
            example = "http")
    @UmlAggregation(label = "speaks")
    private Protocol protocol;

    public Port(final Host host, final int port)
    {
        this(host, Protocol.forPort(port), port);
    }

    public Port(final Host host, final Protocol protocol, final int port)
    {
        this.host = host;
        this.protocol = protocol == null ? Protocol.UNKNOWN : protocol;
        this.port = port;
    }

    public Port(final InetSocketAddress address, final Protocol protocol, final String hostDescription)
    {
        this(new Host(address.getAddress(), hostDescription), protocol, address.getPort());
    }

    protected Port()
    {
    }

    public InetSocketAddress asInetSocketAddress()
    {
        return new InetSocketAddress(host().address(), number());
    }

    public URI asUri()
    {
        try
        {
            return new URI(toString());
        }
        catch (final URISyntaxException e)
        {
            LOGGER.problem(e, "Unable to convert $ to a URI", this);
            return null;
        }
    }

    public Protocol defaultProtocol()
    {
        return Protocol.forPort(port);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Port)
        {
            final var that = (Port) object;
            return port == that.port && host.equals(that.host);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(port, host);
    }

    @KivaKitIncludeProperty
    public Host host()
    {
        return host;
    }

    @JsonIgnore
    public boolean isAvailable()
    {
        try
        {
            // Open the port and close it. If this doesn't throw an exception, the port is not bound.
            new ServerSocket(port).close();
            return true;
        }
        catch (final Exception e)
        {
            return false;
        }
    }

    @JsonIgnore
    public boolean isHttp()
    {
        return protocol().equals(HTTP) || protocol().equals(HTTPS);
    }

    @KivaKitIncludeProperty
    public int number()
    {
        return port;
    }

    public InputStream open()
    {
        try
        {
            return socket().getInputStream();
        }
        catch (final IOException e)
        {
            LOGGER.warning(e, "Unable to open socket $", socket());
        }
        return null;
    }

    public NetworkPath path(final String path)
    {
        return NetworkPath.networkPath(this, path);
    }

    @KivaKitIncludeProperty
    public Protocol protocol()
    {
        return protocol;
    }

    public Port protocol(final Protocol protocol)
    {
        this.protocol = protocol;
        return this;
    }

    public Port resolve()
    {
        return new Port(host, protocol, port);
    }

    public Socket socket()
    {
        try
        {
            final var socket = new Socket(host.address(), port);
            socket.setSoTimeout(Integer.MAX_VALUE);
            socket.setKeepAlive(true);
            return socket;
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Can't open socket", e);
        }
    }

    @Override
    public String toString()
    {
        if (number() == -1 || number() == protocol().defaultPort())
        {
            return host().name();
        }
        return host().name() + ":" + number();
    }
}
