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

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.ftp.internal.lexakai.DiagramSecureFtp;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Connects to servers via the SFTP protocol.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSecureFtp.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
class SecureFtpConnector
{
    private Session session;

    private ChannelSftp channel;

    public SecureFtpConnector(NetworkAccessConstraints ignored)
    {
    }

    public void connect(NetworkLocation location)
    {
        if (!isConnected())
        {
            var jsch = new JSch();
            try
            {
                session = jsch.getSession(location.constraints().userName().toString(),
                        location.host().address().getHostName(), location.port().portNumber());
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(location.constraints().password().toString());
                session.connect();

                channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();
            }
            catch (JSchException e)
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

    public InputStream get(NetworkLocation location)
    {
        // Make sure we are connected.
        connect(location);

        try
        {
            return channel.get(location.networkPath().join());
        }
        catch (SftpException e)
        {
            throw new IllegalStateException("Unable to retrieve file: " + location.networkPath().join(), e);
        }
    }

    /**
     * Copy directly a file from SFTP to some destination
     */
    public void get(NetworkLocation location, Resource destination)
    {
        // Make sure we are connected.
        connect(location);

        var sourcePath = location.networkPath().join();
        try
        {
            channel.get(sourcePath, ((WritableResource) destination).openForWriting());
        }
        catch (SftpException e)
        {
            throw new IllegalStateException("Unable to retrieve file: " + sourcePath, e);
        }
    }

    public boolean isConnected()
    {
        return !(session == null && channel == null);
    }

    @SuppressWarnings({ "unchecked" })
    public ObjectList<LsEntry> listFiles(NetworkLocation location)
    {
        // Make sure we are connected.
        connect(location);

        try
        {
            return new ObjectList<LsEntry>().appendThen(channel.ls(location.networkPath().join()));
        }
        catch (SftpException e)
        {
            throw new IllegalStateException("Unable to list directory contents: " + location.networkPath().join(),
                    e);
        }
    }

    public void safeDisconnect()
    {
        disconnect();
    }
}
