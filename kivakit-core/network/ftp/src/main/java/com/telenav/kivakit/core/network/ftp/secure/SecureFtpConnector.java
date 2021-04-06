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

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramSecureFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.network.core.NetworkPath;
import com.telenav.kivakit.core.resource.WritableResource;

import java.io.InputStream;

@UmlClassDiagram(diagram = DiagramSecureFtp.class)
public class SecureFtpConnector
{
    private final SecureFtpNetworkLocation networkLocation;

    private Session session;

    private ChannelSftp channel;

    public SecureFtpConnector(final SecureFtpNetworkLocation networkLocation,
                              final NetworkAccessConstraints constraints)
    {
        this.networkLocation = networkLocation;
    }

    public void connect()
    {
        if (!isConnected())
        {
            final var jsch = new JSch();
            try
            {
                session = jsch.getSession(networkLocation.constraints().userName().toString(),
                        networkLocation.host().address().getHostName(), networkLocation.port().number());
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(networkLocation.constraints().password().toString());
                session.connect();

                channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();
            }
            catch (final JSchException e)
            {
                safeDisconnect();
                throw new IllegalStateException("Unable to connect", e);
            }
        }
    }

    public void disconnect()
    {
        if (channel != null)
        {
            channel.exit();
        }

        if (session != null)
        {
            session.disconnect();
        }

        channel = null;
        session = null;
    }

    public InputStream get(final NetworkLocation networkLocation)
    {
        // Make sure we are connected.
        connect();

        try
        {
            return channel.get(this.networkLocation.networkPath().join());
        }
        catch (final SftpException e)
        {
            throw new IllegalStateException("Unable to retrieve file: " + this.networkLocation.networkPath().join(), e);
        }
    }

    /**
     * Copy directly a file from SFTP to some destination
     */
    public void get(final NetworkLocation source, final WritableResource destination)
    {
        // Make sure we are connected.
        connect();

        final var sourcePath = source.networkPath().join();
        try
        {
            channel.get(sourcePath, destination.openForWriting());
        }
        catch (final SftpException e)
        {
            throw new IllegalStateException("Unable to retrieve file: " + sourcePath, e);
        }
    }

    public boolean isConnected()
    {
        return !(session == null && channel == null);
    }

    @SuppressWarnings({ "unchecked" })
    public ObjectList<LsEntry> listFiles(final NetworkPath path)
    {
        // Make sure we are connected.
        connect();

        try
        {
            return new ObjectList<LsEntry>().appendAll(channel.ls(networkLocation.networkPath().join()));
        }
        catch (final SftpException e)
        {
            throw new IllegalStateException("Unable to list directory contents: " + networkLocation.networkPath().join(),
                    e);
        }
    }

    public void safeDisconnect()
    {
        disconnect();
    }
}
