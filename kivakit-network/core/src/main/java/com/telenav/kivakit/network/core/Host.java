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
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.objects.reference.ExpiringReference;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.network.core.project.lexakai.diagrams.DiagramPort;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import io.swagger.v3.oas.annotations.media.Schema;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.commandline.SwitchParser.builder;
import static com.telenav.kivakit.commandline.SwitchParser.listSwitchParser;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.network.core.Protocol.FTP;
import static com.telenav.kivakit.network.core.Protocol.HAZELCAST;
import static com.telenav.kivakit.network.core.Protocol.HTTP;
import static com.telenav.kivakit.network.core.Protocol.HTTPS;
import static com.telenav.kivakit.network.core.Protocol.MEMCACHE;
import static com.telenav.kivakit.network.core.Protocol.MONGO;
import static com.telenav.kivakit.network.core.Protocol.MYSQL;
import static com.telenav.kivakit.network.core.Protocol.SFTP;
import static com.telenav.kivakit.network.core.Protocol.UNKNOWN;

/**
 * Represents a host on a network, for which {@link Port}s can be retrieved.
 *
 * <p><b>Attributes</b></p>
 *
 * <p>
 * A host has a network {@link #name()} and a {@link #description()}. If the port is on the localhost, {@link
 * #isLocal()} will return true. If the port can be resolved to an address, {@link #isResolvable()} will return true and
 * {@link #address()} will return the resolved address. The canonical name can be retrieved with {@link
 * #canonicalName()}.
 * </p>
 *
 * <p><b>Ports</b></p>
 *
 * <p>
 * Ports on the given host can be retrieved with:
 * </p>
 *
 * <ul>
 *     <li>{@link #port(int)} - The numbered port</li>
 *     <li>{@link #port(Protocol, int)} - The numbered port with the given protocol</li>
 *     <li>{@link #ftp()}</li>
 *     <li>{@link #ftp(int)}</li>
 *     <li>{@link #hazelcast()}</li>
 *     <li>{@link #hazelcast(int)}</li>
 *     <li>{@link #http()}</li>
 *     <li>{@link #http(int)}</li>
 *     <li>{@link #https()}</li>
 *     <li>{@link #https(int)}</li>
 *     <li>{@link #mongo()}</li>
 *     <li>{@link #mysql()}</li>
 *     <li>{@link #sftp()}</li>
 *     <li>{@link #sftp(int)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A host on the network",
        example = "worker-67.osmteam.telenav.com")
@UmlClassDiagram(diagram = DiagramPort.class)
@LexakaiJavadoc(complete = true)
public class Host implements Named, AsString, Comparable<Host>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final ExpiringReference<Map<String, InetAddress>> inetAddressForName =
            new ExpiringReference<>(Duration.minutes(5))
            {
                @Override
                protected Map<String, InetAddress> onNewObject()
                {
                    return new ConcurrentHashMap<>();
                }
            };

    public static Host NONE = new Host("None");

    public static SwitchParser.Builder<Host> host(final String name, final String description)
    {
        return builder(Host.class)
                .name(name)
                .converter(new Host.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<ObjectList<Host>> hostList(
            final String name, final String description, final String delimiter)
    {
        return listSwitchParser(name, description, new Host.Converter(LOGGER), Host.class, delimiter);
    }

    public static Host local()
    {
        return LocalHost.get();
    }

    public static Host loopback()
    {
        return Loopback.get();
    }

    public static SwitchParser.Builder<NetworkPath> networkFilePath(final String name, final String description)
    {
        return builder(NetworkPath.class)
                .name(name)
                .converter(new NetworkPath.Converter(LOGGER))
                .description(description);
    }

    public static Host parse(final String name)
    {
        return new Host(name);
    }

    public static Host parse(final String name, final String description)
    {
        return new Host(name, description);
    }

    public static SwitchParser.Builder<Port> port(final String name, final String description)
    {
        return builder(Port.class)
                .name(name)
                .converter(new Port.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<ObjectList<Port>> portList(final String name,
                                                                  final String description,
                                                                  final String delimiter)
    {
        return listSwitchParser(name, description, new Port.Converter(LOGGER), Port.class, delimiter);
    }

    /**
     * Converts to and from {@link Host}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Host>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Host onToValue(final String value)
        {
            return parse(value);
        }
    }

    @JsonProperty
    @Schema(description = "A description of the purpose or function of the host")
    private String description;

    @JsonIgnore
    private Boolean local;

    @JsonProperty
    @Schema(description = "The host name, such as eidolon.muppetlabs.com",
            required = true)
    private String name;

    @JsonIgnore
    private transient InetAddress address;

    @JsonIgnore
    private String serializedAddress;

    public Host(final InetAddress address, final String description)
    {
        address(address);
        this.description = description;
    }

    public Host(final InetAddress address, final String name, final String description)
    {
        address(address);
        this.name = name;
        this.description = description;
    }

    protected Host(final String name)
    {
        this.name = name;
    }

    protected Host(final String name, final String description)
    {
        this.name = name;
        this.description = description;
    }

    @UmlExcludeMember
    protected Host()
    {
    }

    @KivaKitIncludeProperty
    public InetAddress address()
    {
        if (address == null)
        {
            resolveAddress();
        }
        return address;
    }

    @Override
    @UmlExcludeMember
    public String asString(final StringFormat format)
    {
        return new ObjectFormatter(this).toString();
    }

    @KivaKitIncludeProperty
    public String canonicalName()
    {
        return address().getCanonicalHostName();
    }

    @Override
    public int compareTo(final Host that)
    {
        return canonicalName().compareTo(that.canonicalName());
    }

    @KivaKitIncludeProperty
    public String description()
    {
        return description;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Host)
        {
            final Host that = (Host) object;
            return address().equals(that.address());
        }
        return false;
    }

    public Port ftp()
    {
        return ftp(FTP.defaultPort());
    }

    public Port ftp(final int port)
    {
        return port(FTP, port);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(address());
    }

    public Port hazelcast()
    {
        return hazelcast(HAZELCAST.defaultPort());
    }

    public Port hazelcast(final int port)
    {
        return port(HAZELCAST, port);
    }

    public Port http()
    {
        return http(HTTP.defaultPort());
    }

    public Port http(final int port)
    {
        return port(HTTP, port);
    }

    public Port https()
    {
        return https(HTTPS.defaultPort());
    }

    public Port https(final int port)
    {
        return port(HTTPS, port);
    }

    @UmlExcludeMember
    public long identifier()
    {
        final var address = address().getAddress();
        if (address.length > 4)
        {
            fail("IPv6 Addresses are not supported");
            return -1;
        }
        var value = 0L;
        for (final var _byte : address)
        {
            value |= _byte;
            value <<= 8;
        }
        return value;
    }

    @KivaKitIncludeProperty
    public boolean isLocal()
    {
        if (local == null)
        {
            final var address = address();
            local = address != null && (address.isAnyLocalAddress() || address.isLoopbackAddress());
        }
        return local;
    }

    @JsonIgnore
    public boolean isResolvable()
    {
        try
        {
            return address() != null;
        }
        catch (final Exception e)
        {
            return false;
        }
    }

    @UmlExcludeMember
    public Port memcache()
    {
        return memcachePort(MEMCACHE.defaultPort());
    }

    @UmlExcludeMember
    public Port memcachePort(final int port)
    {
        return port(MEMCACHE, port);
    }

    public Port mongo()
    {
        return mongo(MONGO.defaultPort());
    }

    public Port mongo(final int port)
    {
        return port(MONGO, port);
    }

    public Port mysql(final int port)
    {
        return port(UNKNOWN, port);
    }

    public Port mysql()
    {
        return mysql(MYSQL.defaultPort());
    }

    @Override
    public String name()
    {
        return isLocal() ? "localhost" : name;
    }

    public Port port(final int number)
    {
        return port(UNKNOWN, number);
    }

    public Port port(final Protocol protocol, final int number)
    {
        return new Port(this, protocol, number);
    }

    public void resolveAddress()
    {
        // If the name is 'localhost' or 127.0.0.1
        if (name.equals("localhost") || name.equals("127.0.0.1"))
        {
            // then the address is the loopback
            address = Loopback.get().address();
        }
        else
        {
            // otherwise, let the subclass resolve the host address
            address = onResolveAddress();
        }
    }

    public Port sftp()
    {
        return sftp(SFTP.defaultPort());
    }

    public Port sftp(final int port)
    {
        return port(SFTP, port);
    }

    @Override
    public String toString()
    {
        final var builder = new StringBuilder();
        builder.append(name());
        if (!name().equals(canonicalName()))
        {
            builder.append(" / ").append(canonicalName());
        }
        if (description != null)
        {
            builder.append("(")
                    .append(description())
                    .append(")");
        }
        return builder.toString();
    }

    @UmlExcludeMember
    protected InetAddress onResolveAddress()
    {
        if (serializedAddress != null)
        {
            return resolve(serializedAddress);
        }
        else
        {
            return resolve(name);
        }
    }

    private void address(final InetAddress address)
    {
        this.address = address;
        serializedAddress = address.getHostAddress();
    }

    private InetAddress resolve(final String name)
    {
        try
        {
            if ("127.0.0.1".equals(name) || "localhost".equals(name) || "localhost.localdomain".equals(name))
            {
                return Loopback.get().address();
            }

            final var map = inetAddressForName.get();
            var address = map.get(name);
            if (address == null)
            {
                address = InetAddress.getByName(name);
                if (address != null)
                {
                    map.put(name, address);
                }
            }
            return address;
        }
        catch (final UnknownHostException e)
        {
            return null;
        }
    }
}
