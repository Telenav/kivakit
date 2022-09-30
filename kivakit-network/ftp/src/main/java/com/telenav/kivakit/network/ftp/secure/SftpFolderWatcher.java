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
import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.collections.watcher.PeriodicCollectionChangeWatcher;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.ftp.internal.lexakai.DiagramSecureFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A change watcher for an SFTP folder.
 *
 * @author matthieun
 * @author jonathanl (shibo)
 * @see PeriodicCollectionChangeWatcher
 */
@UmlClassDiagram(diagram = DiagramSecureFtp.class)
@UmlRelation(label = "connects with", referent = SecureFtpConnector.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class SftpFolderWatcher extends PeriodicCollectionChangeWatcher<NetworkPath>
{
    private boolean initialized;

    @UmlAggregation
    private final SecureFtpSettings credentials;

    private final SecureFtpNetworkLocation location;

    private Map<NetworkPath, Time> lastModifiedCache;

    /**
     * Standard constructor
     *
     * @param frequency The frequency of the checks
     * @param credentials The SFTP credentials object
     * @param location The path of the folder to monitor on the SFTP server
     */
    public SftpFolderWatcher(Frequency frequency,
                             SecureFtpSettings credentials,
                             SecureFtpNetworkLocation location)
    {
        super(frequency);
        lastModifiedCache = new HashMap<>();
        this.credentials = credentials;
        this.location = location;
        information("Watching $ $", location, frequency);
    }

    /**
     * Tells when a {@link NetworkPath} resource is last modified
     *
     * @param object The resource
     * @return The time the resource is last modified
     */
    @Override
    protected Time lastModified(NetworkPath object)
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
        narrate("Checking the SFTP folder ${debug}.", location);
        Collection<NetworkPath> result = new ArrayList<>();

        /*
         * The boolean initialized will force the list initial list of values in the watcher to be
         * empty right after it has been started. That way, any file already present when the first
         * thread checks the folder will automatically trigger the onAdded method.
         */
        if (initialized)
        {
            lastModifiedCache = new HashMap<>();
            var sftp = connector();

            /*
             * Get all the files from the SFTP server
             */
            var path = location.networkPath();
            try
            {
                var files = sftp.listFiles(location);
                for (var file : files)
                {
                    var filePath = path.withChild(file.getFilename());
                    if (!filePath.last().endsWith(".") && !filePath.last().endsWith(".."))
                    {
                        /*
                         * Add the file to the collection and populate the "last Modified" cache map
                         */
                        result.add(filePath);
                        lastModifiedCache.remove(filePath);
                        var lastModified = getTimeLastModified(file);
                        lastModifiedCache.put(filePath, lastModified);
                    }
                }
            }
            finally
            {
                sftp.safeDisconnect();
                narrate("Found ${debug} objects in the folder ${debug}.", result.size(), path.join());
            }
        }
        else
        {
            initialized = true;
        }
        return result;
    }

    /**
     * Returns the SFTP Connector
     */
    private SecureFtpConnector connector()
    {
        var constraints = new NetworkAccessConstraints();
        constraints.timeout(credentials.timeout());
        var location = this.location;
        location.constraints().userName(credentials.userName());
        location.constraints().password(credentials.password());
        return new SecureFtpConnector(constraints);
    }

    /**
     * @param file The SFTP API {@link LsEntry} that represents the file to be considered.
     * @return The last modified time
     */
    private Time getTimeLastModified(LsEntry file)
    {
        return Time.epochMilliseconds(file.getAttrs().getMTime());
    }
}
