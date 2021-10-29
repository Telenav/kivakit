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

import com.telenav.kivakit.kernel.language.collections.map.string.NameMap;
import com.telenav.kivakit.kernel.language.values.name.Name;
import com.telenav.kivakit.network.core.project.lexakai.diagrams.DiagramPort;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.HashMap;
import java.util.Map;

/**
 * An identifier for a particular named protocol
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPort.class)
@LexakaiJavadoc(complete = true)
public class Protocol extends Name
{
    private static final NameMap<Protocol> nameToProtocol = new NameMap<>();

    private static final Map<Integer, Protocol> portToProtocol = new HashMap<>();

    public static final Protocol HTTP = new Protocol("http", 80);

    public static final Protocol HTTPS = new Protocol("https", 443);

    public static final Protocol FTP = new Protocol("ftp", 21);

    public static final Protocol MYSQL = new Protocol("mysql", 3306);

    public static final Protocol SFTP = new Protocol("sftp", 22);

    public static final Protocol UNKNOWN = new Protocol("unknown", -1);

    public static final Protocol MEMCACHE = new Protocol("memcache", 11211);

    public static final Protocol HAZELCAST = new Protocol("hazelcast", 5701);

    public static final Protocol MONGO = new Protocol("mongo", 27017);

    /**
     * @return The protocol with the given name, or null if name doesn't represent a known protocol
     */
    public static Protocol forName(String name)
    {
        return nameToProtocol.get(name);
    }

    /**
     * @return The protocol for the given port number
     */
    public static Protocol forPort(int port)
    {
        return portToProtocol.get(port);
    }

    /** The default port for this protocol */
    private int defaultPort;

    public Protocol(String name, int defaultPort)
    {
        super(name);
        this.defaultPort = defaultPort;
        nameToProtocol.add(this);
        portToProtocol.put(defaultPort, this);
    }

    @UmlExcludeMember
    protected Protocol()
    {
    }

    /**
     * @return The default port for this protocol
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
