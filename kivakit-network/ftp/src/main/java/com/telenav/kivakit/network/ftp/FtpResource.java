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

package com.telenav.kivakit.network.ftp;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.network.core.BaseNetworkResource;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.core.Protocol;
import com.telenav.kivakit.network.ftp.internal.lexakai.DiagramFtp;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Simple FTP downloader. Note that this is made to download a single FTP file and then close the connection. At this
 * point full support of navigating around the FTP file system and examining files is not supported.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFtp.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class FtpResource extends BaseNetworkResource
{
    /**
     * Simple input stream wrapper that will close the FTP connection when reading of the stream is complete.
     *
     * @author ericg
     */
    @CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
                 testing = TESTING_NONE,
                 documentation = DOCUMENTATION_COMPLETE)
    private static class FtpInput extends InputStream
    {
        private final FTPClient client;

        private final InputStream in;

        public FtpInput(FTPClient client, InputStream in)
        {
            this.client = client;
            this.in = in;
        }

        @Override
        public void close() throws IOException
        {
            super.close();

            // Close the wrapped input stream.
            IO.close((Resource) this, in);

            // Logout of the FTP site.
            if (client.isConnected())
            {
                client.logout();
                client.disconnect();
            }
        }

        @Override
        public int read() throws IOException
        {
            return in.read();
        }
    }

    private final FTPClient client = new FTPClient();

    private final NetworkAccessConstraints constraints;

    private final NetworkLocation networkLocation;

    public FtpResource(NetworkLocation location, NetworkAccessConstraints constraints)
    {
        super(location);
        if (!location.protocol().equals(Protocol.FTP))
        {
            throw new IllegalArgumentException("FTP location must use FTP protocol:  " + location);
        }
        networkLocation = location;
        this.constraints = constraints;
    }

    public void close()
    {
        if (isConnected())
        {
            disconnect();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copyTo(WritableResource destination, CopyMode mode, ProgressReporter reporter)
    {
        try
        {
            var in = new BufferedInputStream(openBinaryFileForReading());
            var out = new BufferedOutputStream(destination.openForWriting());
            var buffer = new byte[1024];
            int readCount;
            reporter.start("Copying " + resource());
            while ((readCount = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, readCount);
                reporter.next(Count.count(readCount));
            }
            reporter.end("Copied");
            out.flush();
            out.close();
            in.close();
        }
        catch (IOException e)
        {
            problem(e, "Unable to download file to " + destination, e);
        }
    }

    /**
     * Disconnect the underlying FTP connection
     */
    public void disconnect()
    {
        try
        {
            if (isConnected())
            {
                client.logout();
                client.disconnect();
            }
        }
        catch (IOException e)
        {
            problem(e, "Unable to close the FTP connection.");
        }
    }

    /**
     * Returns true if the FTP client is connected
     */
    public boolean isConnected()
    {
        return client != null && client.isConnected();
    }

    /**
     * @return The files present in the given folder.
     */
    public ObjectList<FTPFile> listOfFiles(NetworkPath path)
    {
        try
        {
            connect();
            var listFiles = client.listFiles(path.join());
            var result = new ObjectList<FTPFile>();
            result.addAll(listFiles);
            return result;
        }
        catch (Exception e)
        {
            return fatal(e, "Unable to list files at $", path);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkLocation location()
    {
        return networkLocation;
    }

    @Override
    public InputStream onOpenForReading()
    {
        try
        {
            connect();

            // Note that we will be transferring ASCII files.
            client.setFileType(FTP.ASCII_FILE_TYPE);

            // Retrieve the file in question.
            return new FtpInput(client, client.retrieveFileStream(networkLocation.networkPath().join()));
        }
        catch (IOException e)
        {
            return fatal(e, "Unable to transfer files");
        }
    }

    /**
     * @return An input stream for reading a binary file on an FTP server (i.e. a gzipped file)
     */
    public InputStream openBinaryFileForReading()
    {
        try
        {
            connect();

            // Note that we will be transferring ASCII files.
            client.setFileType(FTP.BINARY_FILE_TYPE);

            // Retrieve the file in question.
            return new FtpInput(client, client.retrieveFileStream(networkLocation.networkPath().join()));
        }
        catch (IOException e)
        {
            return fatal(e, "Unable to transfer files");
        }
    }

    /**
     * Returns a list of ftp resources
     */
    public ObjectList<FtpResource> resources()
    {
        ObjectList<FtpResource> resources = new ObjectList<>();
        for (var file : listOfFiles(networkLocation.networkPath()))
        {
            var path = networkLocation.networkPath().withChild(file.getName());
            var location = new FtpNetworkLocation(path);
            location.constraints(networkLocation.constraints());
            if (networkLocation instanceof FtpNetworkLocation)
            {
                location.mode(((FtpNetworkLocation) networkLocation).mode());
            }

            resources.add(new FtpResource(location, NetworkAccessConstraints.DEFAULT));
        }
        return resources;
    }

    @Override
    public Bytes sizeInBytes()
    {
        return null;
    }

    /**
     * Connect to the FTP server and login
     */
    private void connect() throws IOException
    {
        // Connect to the server.
        client.connect(networkLocation.host().address());
        var timeoutInMilliseconds = (int) constraints.timeout().asMilliseconds();
        client.setConnectTimeout(timeoutInMilliseconds);
        client.setSoTimeout(timeoutInMilliseconds);
        client.setDefaultTimeout(timeoutInMilliseconds);

        if (networkLocation instanceof FtpNetworkLocation)
        {
            var mode = ((FtpNetworkLocation) networkLocation).mode();
            if (FtpNetworkLocation.Mode.PASSIVE.equals(mode))
            {
                client.enterLocalPassiveMode();
            }
        }

        if (!FTPReply.isPositiveCompletion(client.getReplyCode()))
        {
            fatal("Unable to connect: " + client.getReplyString());
        }

        // Login
        if (networkLocation.constraints().userName() != null && networkLocation.constraints().password() != null)
        {
            if (!client.login(networkLocation.constraints().userName().toString(),
                    networkLocation.constraints().password().toString()))
            {
                fatal("Unable to connect: " + client.getReplyString());
            }
        }
    }
}
