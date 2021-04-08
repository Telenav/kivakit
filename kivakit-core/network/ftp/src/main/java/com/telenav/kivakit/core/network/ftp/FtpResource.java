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

package com.telenav.kivakit.core.network.ftp;

import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.network.core.BaseNetworkResource;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.network.core.NetworkPath;
import com.telenav.kivakit.core.network.core.Protocol;
import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramFtp;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple FTP downloader. Note that this is made to download a single FTP file and then close the connection. At this
 * point full support of navigating around the FTP file system and examining files is not supported.
 *
 * @author ericg
 */
@UmlClassDiagram(diagram = DiagramFtp.class)
@LexakaiJavadoc(complete = true)
public class FtpResource extends BaseNetworkResource
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * Simple input stream wrapper that will close the FTP connection when reading of the stream is complete.
     *
     * @author ericg
     */
    @LexakaiJavadoc(complete = true)
    private static class FtpInput extends InputStream
    {
        private final FTPClient client;

        private final InputStream in;

        public FtpInput(final FTPClient client, final InputStream in)
        {
            this.client = client;
            this.in = in;
        }

        @Override
        public void close() throws IOException
        {
            super.close();

            // Close the wrapped input stream.
            IO.close(in);

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

    private final NetworkAccessConstraints constraints;

    private final NetworkLocation networkLocation;

    private final FTPClient client = new FTPClient();

    public FtpResource(final NetworkLocation location, final NetworkAccessConstraints constraints)
    {
        super(location);
        if (!location.protocol().equals(Protocol.FTP))
        {
            throw new IllegalArgumentException("FTP location must use FTP protocol:  " + location);
        }
        networkLocation = location;
        this.constraints = constraints;
    }

    @Override
    public Bytes bytes()
    {
        return null;
    }

    public void clean()
    {
        if (isConnected())
        {
            disconnect();
        }
    }

    @Override
    public void copyTo(final WritableResource destination, final CopyMode mode, final ProgressReporter reporter)
    {
        try
        {
            final var in = new BufferedInputStream(openBinaryFileForReading());
            final var out = new BufferedOutputStream(destination.openForWriting());
            final var buffer = new byte[1024];
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
        catch (final IOException e)
        {
            throw new IllegalStateException("Unable to download file to " + destination, e);
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
        catch (final IOException e)
        {
            LOGGER.problem(e, "Unable to close the FTP connection.");
        }
    }

    public List<FtpResource> files()
    {
        final List<FtpResource> resources = new ArrayList<>();
        for (final var file : listFiles(networkLocation.networkPath()))
        {
            final var path = networkLocation.networkPath().withChild(file.getName());
            final var location = new FtpNetworkLocation(path);
            location.constraints(networkLocation.constraints());
            if (networkLocation instanceof FtpNetworkLocation)
            {
                location.mode(((FtpNetworkLocation) networkLocation).mode());
            }

            resources.add(new FtpResource(location, NetworkAccessConstraints.DEFAULT));
        }
        return resources;
    }

    public boolean isConnected()
    {
        return client != null && client.isConnected();
    }

    /**
     * @return The files present in the given folder.
     */
    public ObjectList<FTPFile> listFiles(final NetworkPath path)
    {
        try
        {
            connect();
            final var listFiles = client.listFiles(path.join());
            final var result = new ObjectList<FTPFile>();
            result.addAll(listFiles);
            return result;
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Unable to list files at " + path, e);
        }
    }

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
        catch (final IOException e)
        {
            throw new IllegalStateException("Unable to transfer files", e);
        }
    }

    /**
     * @return An input stream for reading a binary file on a FTP server (i.e. a gzipped file)
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
        catch (final IOException e)
        {
            throw new IllegalStateException("Unable to transfer files", e);
        }
    }

    /**
     * Connect the to the FTP server and login
     */
    private void connect() throws IOException
    {
        // Connect to the server.
        client.connect(networkLocation.host().address());
        final var timeoutInMilliseconds = (int) constraints.timeout().asMilliseconds();
        client.setConnectTimeout(timeoutInMilliseconds);
        client.setSoTimeout(timeoutInMilliseconds);
        client.setDefaultTimeout(timeoutInMilliseconds);

        if (networkLocation instanceof FtpNetworkLocation)
        {
            final var mode = ((FtpNetworkLocation) networkLocation).mode();
            if (FtpNetworkLocation.Mode.Passive.equals(mode))
            {
                client.enterLocalPassiveMode();
            }
        }

        if (!FTPReply.isPositiveCompletion(client.getReplyCode()))
        {
            throw new IllegalStateException("Unable to connect: " + client.getReplyString());
        }

        // Login
        if (networkLocation.constraints().userName() != null && networkLocation.constraints().password() != null)
        {
            if (!client.login(networkLocation.constraints().userName().toString(),
                    networkLocation.constraints().password().toString()))
            {
                throw new IllegalStateException("Unable to connect: " + client.getReplyString());
            }
        }
    }
}
