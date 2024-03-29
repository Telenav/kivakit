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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.name.Name;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramPort;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * An identifier for a particular named protocol
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #defaultProtocolForPort(int)}</li>
 *     <li>{@link #defaultPort()}</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseProtocol(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Common Ports</b></p>
 *
 * <ul>
 *     <li>FTP</li>
 *     <li>HAZELCAST</li>
 *     <li>HTTP</li>
 *     <li>HTTPS</li>
 *     <li>MEMCACHE</li>
 *     <li>MONGO</li>
 *     <li>MYSQL</li>
 *     <li>SFTP</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramPort.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class Protocol extends Name
{
    private static final StringMap<Protocol> nameToProtocol = new StringMap<>();

    private static final Map<Integer, Protocol> portToProtocol = new HashMap<>();

    public static final Protocol HTTP = new Protocol("http", 80);

    public static final Protocol HTTPS = new Protocol("https", 443);

    public static final Protocol FTP = new Protocol("ftp", 21);

    public static final Protocol MYSQL = new Protocol("mysql", 3306);

    public static final Protocol SFTP = new Protocol("sftp", 22);

    public static final Protocol UNKNOWN_PROTOCOL = new Protocol("unknown", -1);

    public static final Protocol MEMCACHE = new Protocol("memcache", 11211);

    public static final Protocol HAZELCAST = new Protocol("hazelcast", 5701);

    public static final Protocol MONGO = new Protocol("mongo", 27017);

    /**
     * Returns the protocol for the given port
     *
     * @return The protocol for the given port number
     */
    public static Protocol defaultProtocolForPort(int port)
    {
        return portToProtocol.get(port);
    }

    /**
     * Parses the given protocol name into a {@link Protocol}
     *
     * @return The protocol with the given name, or null if name doesn't represent a known protocol
     */
    public static Protocol parseProtocol(Listener listener, String name)
    {
        return nameToProtocol.get(name);
    }

    /** The default port for this protocol */
    private int defaultPort;

    public Protocol(String name, int defaultPort)
    {
        super(name);
        this.defaultPort = defaultPort;
        nameToProtocol.put(name(), this);
        portToProtocol.put(defaultPort, this);
    }

    protected Protocol()
    {
    }

    /**
     * Returns the default port for this protocol
     */
    public int defaultPort()
    {
        return defaultPort;
    }

    @Override
    public String toString()
    {
        return name();
    }
}
