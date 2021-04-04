////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.ftp.secure;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramSecureFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.collections.watcher.PeriodicCollectionChangeWatcher;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Watcher for a SFTP folder
 *
 * @author matthieun
 */
@UmlClassDiagram(diagram = DiagramSecureFtp.class)
@UmlRelation(label = "connects with", referent = SecureFtpConnector.class)
public class SftpFolderWatcher extends PeriodicCollectionChangeWatcher<NetworkPath>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private boolean initialized;

    @UmlAggregation
    private final SecureFtpSettings credentials;

    private final NetworkPath fullSftpFolderPath;

    private Map<NetworkPath, Time> lastModifiedCache;

    /**
     * Standard constructor
     *
     * @param frequency The frequency of the checks
     * @param credentials The SFTP credentials object
     * @param fullSftpFolderPath The path of the folder to monitor on the SFTP server
     */
    public SftpFolderWatcher(final Frequency frequency, final SecureFtpSettings credentials,
                             final NetworkPath fullSftpFolderPath)
    {
        super(frequency);
        lastModifiedCache = new HashMap<>();
        this.credentials = credentials;
        this.fullSftpFolderPath = fullSftpFolderPath;
        LOGGER.information("Created a Folder Watcher ${debug} with frequency ${debug}.", fullSftpFolderPath.join(),
                frequency.toString());
    }

    /**
     * Tells when a {@link NetworkPath} resource is last modified
     *
     * @param object The resource
     * @return The time the resource is last modified
     */
    @Override
    protected Time lastModified(final NetworkPath object)
    {
        if (lastModifiedCache.containsKey(object))
        {
            // The object has already been cached.
            return lastModifiedCache.get(object);
        }
        else
        {
            // The object has not already been cached. List the available objects, and retrieve the "last modified" value.
            objects();
            if (lastModifiedCache.containsKey(object))
            {
                return lastModifiedCache.get(object);
            }
            else
            {
                throw new IllegalStateException(
                        "The file " + object.join() + " does not exist, neither does its last modified time.");
            }
        }
    }

    /**
     * List the {@link NetworkPath}s that are present in the SFTP folder.
     *
     * @return The {@link NetworkPath}s that are present in the SFTP folder.
     */
    @Override
    protected Collection<NetworkPath> objects()
    {
        LOGGER.narrate("Checking the SFTP folder ${debug}.", fullSftpFolderPath.join());
        final Collection<NetworkPath> result = new ArrayList<>();

        /*
         * The boolean initialized will force the list initial list of values in the watcher to be
         * empty right after it has been started. That way, any file already present when the first
         * thread checks the folder will automatically trigger the onAdded method.
         */
        if (initialized)
        {
            lastModifiedCache = new HashMap<>();
            final var sftp = getSftpConnector();

            /*
             * Get all the files from the SFTP server
             */
            try
            {
                final var files = sftp.listFiles(fullSftpFolderPath);
                for (final var file : files)
                {
                    final var filePath = fullSftpFolderPath.withChild(file.getFilename());
                    if (!filePath.last().endsWith(".") && !filePath.last().endsWith(".."))
                    {
                        /*
                         * Add the file to the collection and populate the "last Modified" cache map
                         */
                        result.add(filePath);
                        lastModifiedCache.remove(filePath);
                        final var lastModified = getTimeLastModified(file);
                        lastModifiedCache.put(filePath, lastModified);
                    }
                }
            }
            finally
            {
                sftp.safeDisconnect();
                LOGGER.narrate("Found ${debug} objects in the folder ${debug}.", result.size(),
                        fullSftpFolderPath.join());
            }
        }
        else
        {
            initialized = true;
        }
        return result;
    }

    /**
     * @return The SFTP Connector
     */
    private SecureFtpConnector getSftpConnector()
    {
        final var constraints = new NetworkAccessConstraints();
        constraints.timeout(credentials.timeout());
        final var location = new SecureFtpNetworkLocation(fullSftpFolderPath);
        location.constraints().userName(credentials.userName());
        location.constraints().password(credentials.password());
        return new SecureFtpConnector(location, constraints);
    }

    /**
     * @param file The SFTP API {@link LsEntry} that represents the file to be considered.
     * @return The last modified time
     */
    private Time getTimeLastModified(final LsEntry file)
    {
        return Time.milliseconds(file.getAttrs().getMTime());
    }
}
