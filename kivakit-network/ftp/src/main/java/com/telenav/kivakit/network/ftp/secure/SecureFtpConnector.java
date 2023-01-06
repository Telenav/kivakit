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
import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.ftp.internal.lexakai.DiagramSecureFtp;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;

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
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
class SecureFtpConnector
{
    private Session session;

    private ChannelSftp channel;

    public SecureFtpConnector(NetworkAccessConstraints ignored)
    {
    }

    /**
     * Connects to the given network location
     *
     * @param location The location to connect to
     * @throws IllegalStateException Thrown if connecting fails
     */
    public void connect(NetworkLocation location) throws IllegalStateException
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
                disconnect();
                illegalState(e, "Could not connect to: $", location);
            }
        }
    }

    /**
     * Disconnects from any connected remote FTP server
     */
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

    /**
     * Returns an input stream to the FTP resource at the given location
     *
     * @param location The location
     * @return The input stream
     * @throws IllegalStateException Thrown if the resource cannot be accessed
     */
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
            return illegalState(e, "Unable to retrieve file: $", location.networkPath().join());
        }
    }

    /**
     * Copy directly a file from SFTP to some destination
     *
     * @param location The location
     * @param destination The destination resource to write to
     * @throws IllegalStateException Thrown if the resource cannot be accessed
     */
    public void get(NetworkLocation location, WritableResource destination)
    {
        // Make sure we are connected.
        connect(location);

        var sourcePath = location.networkPath().join();
        try
        {
            channel.get(sourcePath, destination.openForWriting());
        }
        catch (SftpException e)
        {
            illegalState("Unable to retrieve file: " + sourcePath, e);
        }
    }

    /**
     * Returns true if this connector is connected
     */
    public boolean isConnected()
    {
        return !(session == null && channel == null);
    }

    /**
     * Returns a list of entries at the given location
     *
     * @param location The location
     * @throws IllegalStateException Thrown if the resource cannot be accessed
     */
    @SuppressWarnings({ "unchecked" })
    public ObjectList<LsEntry> listFiles(NetworkLocation location)
    {
        // Make sure we are connected.
        connect(location);

        try
        {
            return new ObjectList<LsEntry>().appending(channel.ls(location.networkPath().join()));
        }
        catch (SftpException e)
        {
            return illegalState(e, "Unable to list directory contents: " + location.networkPath().join());
        }
    }
}
