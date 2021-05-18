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

package com.telenav.kivakit.network.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.data.conversion.string.collection.BaseListConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.network.core.project.lexakai.diagrams.DiagramPort;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
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

import static com.telenav.kivakit.network.core.Protocol.HTTP;
import static com.telenav.kivakit.network.core.Protocol.HTTPS;

/**
 * A host, port and protocol. The port has a number, accessible with {@link #number()}, it exists on a {@link Host} and
 * it speaks a given {@link #protocol()}.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A unique port on the network that communicates with clients using a specific protocol")
@UmlClassDiagram(diagram = DiagramPort.class)
public class Port
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return A port object from the given {@link URI}
     */
    public static Port from(final URI uri)
    {
        final var host = new Host(uri.getHost());
        final var scheme = uri.getScheme();
        final var port = uri.getPort();
        final var protocol = Protocol.forName(scheme);
        return new Port(host, protocol, port);
    }

    /**
     * Parses a port of the form "[host]:[port-number]". If the port number is omitted, port 80 is assumed.
     *
     * @return A port for the given string or null if the string is not a port
     */
    public static Port parse(final String port)
    {
        return new Converter(LOGGER).convert(port);
    }

    /**
     * Converts to and from a {@link Port}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
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

    /**
     * Converts to and from a list of ports
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
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

    /**
     * @return The socket address for this port
     */
    public InetSocketAddress asInetSocketAddress()
    {
        return new InetSocketAddress(host().address(), number());
    }

    /**
     * @return The URI for this port
     */
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

    /**
     * @return The default protocol for this port based on the port number
     */
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

    /**
     * @return The host that owns this port
     */
    @KivaKitIncludeProperty
    public Host host()
    {
        return host;
    }

    /**
     * @return True if this port's port number is not claimed on the local host
     */
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

    /**
     * @return True if this port speaks HTTP
     */
    @JsonIgnore
    public boolean isHttp()
    {
        return protocol().equals(HTTP) || protocol().equals(HTTPS);
    }

    /**
     * @return The port number
     */
    @KivaKitIncludeProperty
    public int number()
    {
        return port;
    }

    /**
     * @return A socket input stream for this port
     */
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

    /**
     * @return The {@link NetworkPath} at the given path on this host and port
     */
    public NetworkPath path(final String path)
    {
        return NetworkPath.networkPath(this, path);
    }

    /**
     * @return The protocol spoken by this port
     */
    @KivaKitIncludeProperty
    public Protocol protocol()
    {
        return protocol;
    }

    /**
     * @param protocol The protocol to assign to this port
     */
    public Port protocol(final Protocol protocol)
    {
        this.protocol = protocol;
        return this;
    }

    /**
     * @return This port resolved
     */
    public Port resolve()
    {
        return new Port(host, protocol, port);
    }

    /**
     * A socket for this port, with an infinite timeout and keep alive
     */
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
