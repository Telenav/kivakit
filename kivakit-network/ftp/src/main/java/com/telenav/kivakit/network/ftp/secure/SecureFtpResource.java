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

package com.telenav.kivakit.network.ftp.secure;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.network.core.BaseNetworkResource;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.ftp.internal.lexakai.DiagramSecureFtp;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.compression.codecs.GzipCodec;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.InputStream;

/**
 * A resource accessed by SFTP. A list of files can be retrieved with {@link #listFiles()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramSecureFtp.class)
@LexakaiJavadoc(complete = true)
public class SecureFtpResource extends BaseNetworkResource
{
    private final SecureFtpConnector connector;

    @UmlAggregation(label = "accesses")
    private final NetworkLocation location;

    public SecureFtpResource(NetworkLocation location, NetworkAccessConstraints constraints)
    {
        super(location);
        if (!(location instanceof SecureFtpNetworkLocation))
        {
            illegalArgument("SFTP request must use an sftp network location:  " + location);
        }
        connector = new SecureFtpConnector(constraints);
        this.location = location;

        if (location.networkPath().fileName().endsWith(".gz"))
        {
            codec(new GzipCodec());
        }
    }

    /**
     * Copy this resource to the disk
     */
    @Override
    public void copyTo(WritableResource destination, CopyMode mode, ProgressReporter reporter)
    {
        try
        {
            connector.connect(location);
            reporter.start();
            connector.get(location, destination);
            reporter.end();
        }
        finally
        {
            disconnect();
        }
    }

    public void disconnect()
    {
        connector.safeDisconnect();
    }

    /**
     * @return The files present in current folder.
     */
    public ObjectList<LsEntry> listFiles()
    {
        connector.connect(location);
        return connector.listFiles(location);
    }

    @Override
    public NetworkLocation location()
    {
        return location;
    }

    @Override
    @UmlRelation(label = "reads")
    public InputStream onOpenForReading()
    {
        connector.connect(location);
        return SecureFtpInput.forConnectorAndLocation(connector, location);
    }

    @Override
    public Bytes sizeInBytes()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return asUri().toString();
    }
}
