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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.AsString;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramPort;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.commandline.SwitchParser.switchParser;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.messaging.Listener.consoleListener;
import static com.telenav.kivakit.network.core.NetworkPath.networkPath;
import static com.telenav.kivakit.network.core.Protocol.HTTP;
import static com.telenav.kivakit.network.core.Protocol.HTTPS;
import static com.telenav.kivakit.network.core.Protocol.UNKNOWN_PROTOCOL;
import static com.telenav.kivakit.network.core.Protocol.defaultProtocolForPort;
import static com.telenav.kivakit.network.core.Protocol.parseProtocol;

/**
 * A host, port and protocol. The port has a number, accessible with {@link #portNumber()}, it exists on a {@link Host}
 * and it speaks a given {@link #protocol()}.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #port(URI)}</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parsePort(Listener, String)}</li>
 *     <li>{@link #portSwitchParser(Listener, String, String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #defaultProtocol()}</li>
 *     <li>{@link #host()}</li>
 *     <li>{@link #isAvailable()}</li>
 *     <li>{@link #isHttp()}</li>
 *     <li>{@link #portNumber()}</li>
 *     <li>{@link #protocol()}</li>
 *     <li>{@link #protocol(Protocol)}</li>
 * </ul>
 *
 * <p><b>Paths</b></p>
 *
 * <ul>
 *     <li>{@link #path(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asUri(Listener)}</li>
 *     <li>{@link #asString(Format)}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #open(Listener)}</li>
 *     <li>{@link #resolve()}</li>
 *     <li>{@link #socket()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramPort.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Port implements AsString
{
    /**
     * Parses a port of the form "[host]:[port-number]". If the port number is omitted, port 80 is assumed.
     *
     * @return A port for the given string or null if the string is not a port
     */
    public static Port parsePort(Listener listener, String port)
    {
        return new Converter(listener).convert(port);
    }

    /**
     * Returns a port object from the given {@link URI}
     */
    public static Port port(URI uri)
    {
        var host = new Host(uri.getHost());
        var scheme = uri.getScheme();
        var port = uri.getPort();
        var protocol = parseProtocol(consoleListener(), scheme);
        return new Port(host, port, protocol);
    }

    /**
     * Returns a switch parser builder for parsing ports
     *
     * @param listener The listener to send problems to
     * @param name The name of the switch
     * @param description The switch description
     * @return The switch parser builder
     */
    public static SwitchParser.Builder<Port> portSwitchParser(Listener listener, String name, String description)
    {
        return switchParser(Port.class)
            .name(name)
            .converter(new Port.Converter(listener))
            .description(description);
    }

    /**
     * Converts to and from a {@link Port}
     *
     * @author jonathanl (shibo)
     */
    @CodeQuality(stability = STABLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    public static class Converter extends BaseStringConverter<Port>
    {
        /** Host converter */
        private final Host.Converter hostConverter;

        /** Integer converter for port numbers */
        private final IntegerConverter integerConverter;

        public Converter(Listener listener)
        {
            super(listener);
            integerConverter = new IntegerConverter(listener);
            hostConverter = new Host.Converter(listener);
        }

        @Override
        protected Port onToValue(String value)
        {
            // It is expected that the port is in the format <host>:<portNumber>. If
            // no <portNumber> is specified then 80 is assumed.
            var parts = value.split(":");
            var host = hostConverter.convert(parts[0]);
            var portNumber = 80;
            if (parts.length > 1)
            {
                var portInteger = integerConverter.convert(parts[1]);
                if (portInteger != null)
                {
                    portNumber = portInteger;
                }
            }
            return new Port(host, portNumber);
        }
    }

    /** The host */
    @UmlAggregation
    private Host host;

    /** The port */
    private int portNumber;

    /** The protocol that this port speaks */
    @UmlAggregation(label = "speaks")
    private Protocol protocol;

    /**
     * @param host The host
     * @param portNumber The port
     */
    public Port(Host host, int portNumber)
    {
        this(host, portNumber, defaultProtocolForPort(portNumber));
    }

    /**
     * @param host The host
     * @param portNumber The port
     * @param protocol The protocol
     */
    public Port(Host host, int portNumber, Protocol protocol)
    {
        this.host = host;
        this.protocol = protocol == null ? UNKNOWN_PROTOCOL : protocol;
        this.portNumber = portNumber;
    }

    /**
     * @param address The internet address
     * @param protocol The communication protocol
     * @param hostDescription The host description
     */
    public Port(InetSocketAddress address, Protocol protocol, String hostDescription)
    {
        this(new Host(address.getAddress(), hostDescription), address.getPort(), protocol);
    }

    protected Port()
    {
    }

    /**
     * Returns the socket address for this port
     */
    public InetSocketAddress asInetSocketAddress()
    {
        return new InetSocketAddress(host().address(), portNumber());
    }

    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public String asString(@NotNull Format format)
    {
        return switch (format)
            {
                case PROGRAMMATIC -> protocol() + "://" + host().name() + ":" + portNumber();
                default -> toString();
            };
    }

    /**
     * Returns the URI for this port
     */
    public URI asUri(Listener listener)
    {
        try
        {
            return new URI(toString());
        }
        catch (URISyntaxException e)
        {
            listener.problem(e, "Unable to convert $ to a URI", this);
            return null;
        }
    }

    /**
     * Returns the default protocol for this port based on the port number
     */
    public Protocol defaultProtocol()
    {
        return defaultProtocolForPort(portNumber);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Port that)
        {
            return portNumber == that.portNumber && host.equals(that.host);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashMany(portNumber, host);
    }

    /**
     * Returns the host that owns this port
     */
    @FormatProperty
    public Host host()
    {
        return host;
    }

    /**
     * Returns true if this port's port number is not claimed on the local host
     */
    public boolean isAvailable()
    {
        try
        {
            // Open the port and close it. If this doesn't throw an exception, the port is not bound.
            new ServerSocket(portNumber).close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Returns true if this port speaks HTTP
     */
    public boolean isHttp()
    {
        return protocol().equals(HTTP) || protocol().equals(HTTPS);
    }

    /**
     * Returns a socket input stream for this port
     */
    public InputStream open(Listener listener)
    {
        try
        {
            return socket().getInputStream();
        }
        catch (IOException e)
        {
            listener.warning(e, "Unable to open socket $", socket());
        }
        return null;
    }

    /**
     * Returns the {@link NetworkPath} at the given path on this host and port
     */
    public NetworkPath path(Listener listener, String path)
    {
        return networkPath(listener, this, path);
    }

    /**
     * Returns the {@link NetworkPath} at the given path on this host and port
     */
    public NetworkPath path(String path)
    {
        return networkPath(this, path);
    }

    /**
     * Returns the port number
     */
    @FormatProperty
    public int portNumber()
    {
        return portNumber;
    }

    /**
     * Returns the protocol spoken by this port
     */
    @FormatProperty
    public Protocol protocol()
    {
        return protocol;
    }

    /**
     * @param protocol The protocol to assign to this port
     */
    public Port protocol(Protocol protocol)
    {
        this.protocol = protocol;
        return this;
    }

    /**
     * Returns this port resolved
     */
    public Port resolve()
    {
        return new Port(host, portNumber, protocol);
    }

    /**
     * A socket for this port, with an infinite timeout and keep alive
     */
    public Socket socket()
    {
        try
        {
            var socket = new Socket(host.address(), portNumber);
            socket.setSoTimeout(Integer.MAX_VALUE);
            socket.setKeepAlive(true);
            return socket;
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Can't open socket", e);
        }
    }

    @Override
    public String toString()
    {
        if (portNumber() == -1 || portNumber() == protocol().defaultPort())
        {
            return host().name();
        }
        return host().name() + ":" + portNumber();
    }
}
