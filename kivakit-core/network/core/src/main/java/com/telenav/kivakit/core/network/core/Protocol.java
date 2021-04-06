////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.network.core.project.lexakai.diagrams.DiagramPort;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.kivakit.core.kernel.language.collections.map.string.NameMap;
import com.telenav.kivakit.core.kernel.language.values.name.Name;

import java.util.HashMap;
import java.util.Map;

@UmlClassDiagram(diagram = DiagramPort.class)
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

    public static Protocol forName(final String name)
    {
        return nameToProtocol.get(name);
    }

    public static Protocol forPort(final int port)
    {
        return portToProtocol.get(port);
    }

    private int defaultPort;

    public Protocol(final String name, final int defaultPort)
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
