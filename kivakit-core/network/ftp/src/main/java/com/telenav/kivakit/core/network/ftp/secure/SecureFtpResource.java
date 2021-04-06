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

package com.telenav.kivakit.core.network.ftp.secure;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramSecureFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.network.core.BaseNetworkResource;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.compression.codecs.GzipCodec;

@UmlClassDiagram(diagram = DiagramSecureFtp.class)
public class SecureFtpResource extends BaseNetworkResource
{
    private final SecureFtpConnector connector;

    @UmlAggregation(label = "accesses")
    private final NetworkLocation networkLocation;

    public SecureFtpResource(final NetworkLocation networkLocation, final NetworkAccessConstraints constraints)
    {
        super(networkLocation);
        if (!(networkLocation instanceof SecureFtpNetworkLocation))
        {
            throw new IllegalArgumentException("SFTP request must use an sftp network location:  " + networkLocation);
        }
        connector = new SecureFtpConnector((SecureFtpNetworkLocation) networkLocation, constraints);
        this.networkLocation = networkLocation;

        if (networkLocation.networkPath().fileName().endsWith(".gz"))
        {
            codec(new GzipCodec());
        }
    }

    @Override
    public Bytes bytes()
    {
        return null;
    }

    public void clean()
    {
        connector.safeDisconnect();
    }

    /**
     * Copy this resource to the disk
     */
    @Override
    public void copyTo(final WritableResource destination, final CopyMode mode, final ProgressReporter reporter)
    {
        try
        {
            connector.connect();
            reporter.start();
            connector.get(networkLocation, destination);
            reporter.end();
        }
        finally
        {
            clean();
        }
    }

    /**
     * @return The files present in current folder.
     */
    public ObjectList<LsEntry> listFiles()
    {
        connector.connect();
        return connector.listFiles(networkLocation.networkPath());
    }

    @Override
    public NetworkLocation location()
    {
        return networkLocation;
    }

    @Override
    @UmlRelation(label = "reads")
    public SecureFtpInput onOpenForReading()
    {
        connector.connect();
        return SecureFtpInput.forConnectorAndLocation(connector, networkLocation);
    }

    @Override
    public String toString()
    {
        return asUri().toString();
    }
}
