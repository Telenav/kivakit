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

import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.map.CacheMap;
import com.telenav.kivakit.core.language.Arrays;
import com.telenav.kivakit.core.language.object.ObjectFormatter;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.network.core.project.lexakai.DiagramPort;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import static com.telenav.kivakit.commandline.SwitchParser.builder;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
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
@SuppressWarnings("SpellCheckingInspection") @UmlClassDiagram(diagram = DiagramPort.class)
@LexakaiJavadoc(complete = true)
public class Host extends BaseRepeater implements
        Named,
        Stringable,
        Comparable<Host>
{
    private static final CacheMap<String, InetAddress> resolvedHostNames =
            new CacheMap<>(Maximum.maximum(2048), Duration.minutes(5));

    public static SwitchParser.Builder<Host> hostSwitchParser(Listener listener, String name, String description)
    {
        return builder(Host.class)
                .name(name)
                .converter(new Host.Converter(listener))
                .description(description);
    }

    public static Host local()
    {
        return LocalHost.get();
    }

    public static Host loopback()
    {
        return Loopback.get();
    }

    public static Host none()
    {
        return new Host("[No Host]");
    }

    public static Host parseHost(Listener listener, String name)
    {
        return listener.listenTo(new Host(name));
    }

    public static Host parseHost(Listener listener, String name, String description)
    {
        return listener.listenTo(new Host(name, description));
    }

    /**
     * Converts to and from {@link Host}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Host>
    {
        public Converter(Listener listener)
        {
            super(listener, Host::parseHost);
        }
    }

    private transient InetAddress address;

    private String description;

    private Boolean local;

    private String name;

    private byte[] rawAddress;

    public Host(InetAddress address, String description)
    {
        address(ensureNotNull(address));
        this.description = description;
    }

    public Host(InetAddress address, String name, String description)
    {
        address(ensureNotNull(address));
        this.name = name;
        this.description = description;
    }

    protected Host(String name)
    {
        this.name = ensureNotNull(name);
    }

    protected Host(String name, String description)
    {
        this(name);
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
    public String asString(Format format)
    {
        return new ObjectFormatter(this).toString();
    }

    @KivaKitIncludeProperty
    public String canonicalName()
    {
        return address().getCanonicalHostName();
    }

    @Override
    public int compareTo(Host that)
    {
        return canonicalName().compareTo(that.canonicalName());
    }

    @KivaKitIncludeProperty
    public String description()
    {
        return description;
    }

    public String dnsName()
    {
        return address().getHostName();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Host)
        {
            Host that = (Host) object;
            if (name().equals(name()))
            {
                return true;
            }
            return address().equals(that.address());
        }
        return false;
    }

    public Port ftp()
    {
        return ftp(FTP.defaultPort());
    }

    public Port ftp(int port)
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

    public Port hazelcast(int port)
    {
        return port(HAZELCAST, port);
    }

    public Port http()
    {
        return http(HTTP.defaultPort());
    }

    public Port http(int port)
    {
        return port(HTTP, port);
    }

    public Port https()
    {
        return https(HTTPS.defaultPort());
    }

    public Port https(int port)
    {
        return port(HTTPS, port);
    }

    @UmlExcludeMember
    public long identifier()
    {
        var address = address().getAddress();
        if (address.length > 4)
        {
            problem("IPv6 Addresses are not supported");
            return -1;
        }
        var value = 0L;
        for (var _byte : address)
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
            var address = address();
            local = address != null && (address.isAnyLocalAddress() || address.isLoopbackAddress());
        }
        return local;
    }

    public boolean isResolvable()
    {
        try
        {
            return address() != null;
        }
        catch (Exception e)
        {
            problem("Address not resolvable");
            return false;
        }
    }

    @UmlExcludeMember
    public Port memcache()
    {
        return memcachePort(MEMCACHE.defaultPort());
    }

    @UmlExcludeMember
    public Port memcachePort(int port)
    {
        return port(MEMCACHE, port);
    }

    public Port mongo()
    {
        return mongo(MONGO.defaultPort());
    }

    public Port mongo(int port)
    {
        return port(MONGO, port);
    }

    public Port mysql(int port)
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

    public Port port(int number)
    {
        return port(UNKNOWN, number);
    }

    public Port port(Protocol protocol, int number)
    {
        return new Port(this, protocol, number);
    }

    public Port sftp()
    {
        return sftp(SFTP.defaultPort());
    }

    public Port sftp(int port)
    {
        return port(SFTP, port);
    }

    @Override
    public String toString()
    {
        var builder = new StringBuilder();
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
        if (rawAddress != null)
        {
            try
            {
                return InetAddress.getByAddress(rawAddress);
            }
            catch (UnknownHostException e)
            {
                problem(e, "Can't resolve address: $", Arrays.asHexadecimalString(rawAddress));
            }
        }
        else if (name != null)
        {
            return resolveHostName(name);
        }
        else
        {
            problem("Cannot resolve address: no name or raw address available");
        }
        return null;
    }

    private void address(InetAddress address)
    {
        this.address = address;
        rawAddress = address.getAddress();
    }

    private void resolveAddress()
    {
        // If the name is 'localhost' or 127.0.0.1
        if (name.equals("localhost"))
        {
            try
            {
                address = InetAddress.getLocalHost();
            }
            catch (UnknownHostException e)
            {
                problem(e, "Unable to resolve local host");
            }
        }
        else if (name.equals("127.0.0.1"))
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

    private InetAddress resolveHostName(String name)
    {
        try
        {
            //noinspection SpellCheckingInspection
            if ("127.0.0.1".equals(name)
                    || "localhost".equals(name)
                    || "localhost.localdomain".equals(name))
            {
                return Loopback.get().address();
            }

            var address = resolvedHostNames.get(name);
            if (address == null)
            {
                address = InetAddress.getByName(name);
                if (address != null)
                {
                    resolvedHostNames.put(name, address);
                }
            }
            return address;
        }
        catch (UnknownHostException e)
        {
            problem(e, "Unable to resolve host name: $", name);
            return null;
        }
    }
}
