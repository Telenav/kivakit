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

package com.telenav.kivakit.filesystems.hdfs.proxy;

import com.telenav.kivakit.core.configuration.InstanceIdentifier;
import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.filesystem.spi.FileSystemService;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.resource.ResourceFolder;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.filesystems.hdfs.proxy.project.lexakai.diagrams.DiagramHdfsProxy;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * <b>Not Public API</b>
 *
 * <p><b>Loading and Configuration of HDFS FileSystems</b></p>
 *
 * <p>
 * HDFS filesystem wrapper used by HdfsFileSystemService to implement the KivaKit {@link FileSystemService} service
 * provider interface. The {@link #of(FilePath)} method here lazy-loads and configures an HdfsFileSystem object which
 * gives access to the Hadoop {@link FileSystem} interface for the cluster specified in the given HDFS file path. Note
 * that this means that multiple HDFS filesystems can be accessed at the same time and transparently. See
 * FileSystemServiceLoader for details on how the KivaKit dynamically loads {@link FileSystemService} implementations.
 * </p>
 *
 * <p><b>Transparent Access to HDFS Files and Folders</b></p>
 *
 * <p>
 * Because HDFS filesystems are loaded based on {@link FilePath} parsing, the end-user only has to include the HDFS
 * module in their project and they can then use HDFS file paths with {@link File} and {@link Folder} as they would use
 * any other path.
 * </p>
 *
 * <p>
 * For example, to access a file on cluster 'my-cluster', this module can be included and the following file object
 * constructed. FileSystemServiceLoader will load the HdfsFileSystemService for the "hdfs" scheme and that service will
 * then configure a Hadoop {@link FileSystem} instance (using this class) for the 'my-cluster' cluster. All of this
 * occurs automatically when a file object is constructed like this:
 * </p>
 *
 * <pre>
 * new File("hdfs://my-cluster/test.txt");
 * </pre>
 *
 * <p><b>Configuration of HDFS Clusters</b></p>
 *
 * <p>
 * HDFS clustered filesystems are configured with an hdfs-site.xml file and optionally a yarn-site.xml file. KivaKit
 * locates these files by looking under the configuration folder specified by {@link HdfsProxyServerSettings} in the
 * folder named for the cluster. For example:
 * </p>
 *
 * <pre>
 * [...]/configurations/my-cluster/hdfs-site.xml
 * [...]/configurations/my-other-cluster/hdfs-site.xml
 * </pre>
 */
@UmlClassDiagram(diagram = DiagramHdfsProxy.class)
@LexakaiJavadoc(complete = true)
class HdfsFileSystem
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    // Map from cluster name to file system
    private static final Map<String, HdfsFileSystem> filesystems = new HashMap<>();

    private static final Debug DEBUG = new Debug(LOGGER);

    /**
     * Loads
     */
    public synchronized static HdfsFileSystem of(final FilePath path)
    {
        return filesystems.computeIfAbsent(cluster(path), ignored -> new HdfsFileSystem(path.root()));
    }

    @KivaKitIncludeProperty
    private final FilePath root;

    // The HDFS file system
    private FileSystem fileSystem;

    private HdfsFileSystem(final FilePath root)
    {
        this.root = root;

        // Ensure validity of HDFS cluster in path
        cluster(root);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * @return The cluster name such as "navhacluster" from an HDFS path like hdfs://navhacluster
     */
    private static String cluster(final FilePath path)
    {
        final var pattern = java.util.regex.Pattern.compile("hdfs://([^/]*)");
        final var matcher = pattern.matcher(path.toString());
        if (matcher.lookingAt())
        {
            return matcher.group(1);
        }
        return fail("HDFS path '$' is not valid", path);
    }

    @KivaKitIncludeProperty
    FileSystem fileSystem()
    {
        if (fileSystem == null)
        {
            try
            {
                final var instance = new InstanceIdentifier(root.first());
                final var settings = Settings.require(HdfsProxyServerSettings.class, instance);
                fileSystem = settings.user().doAs((PrivilegedExceptionAction<FileSystem>) () ->
                {
                    DEBUG.trace("Initializing HDFS at $", root);
                    final var uri = URI.create(root.toString());
                    final var configuration = hdfsConfiguration(root);
                    configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
                    configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
                    return FileSystem.get(uri, configuration);
                });
            }
            catch (final Exception e)
            {
                LOGGER.problem(e, "Unable to initialize HDFS filesystem");
            }
        }
        return fileSystem;
    }

    Path hdfsPath(final FilePath path)
    {
        final var rootString = root.toString();
        final var pathString = path.toString();
        return new Path("/" + pathString.substring(rootString.length()));
    }

    private Configuration hdfsConfiguration(final FilePath path)
    {
        // If the user defines KIVAKIT_HDFS_CONFIGURATION_FOLDER,
        ResourceFolder configurationFolder = null;
        final var property = JavaVirtualMachine.property("KIVAKIT_HDFS_CONFIGURATION_FOLDER");
        if (property != null)
        {
            // see if the folder exists or can be created
            final var folder = Folder.parse(property);
            if (folder != null)
            {
                folder.mkdirs();
                configurationFolder = folder;
            }
            else
            {
                fail("HDFS configuration folder $ does not exist", folder);
            }
        }

        // If the user didn't provide an explicit configuration folder on the command line,
        if (configurationFolder == null)
        {
            // then try the resource specified in the HdfsProxyServerSettings configuration,
            final var settings = Settings.require(HdfsProxyServerSettings.class);
            configurationFolder = settings.configurationFolder();
        }

        ensureNotNull(configurationFolder, "Cannot initialize HDFS as no HdfsProxyServerSettings was provided");

        // Get resource folder holding site configuration file
        final var siteConfiguration = configurationFolder
                .folder(cluster(path))
                .resource("/hdfs-site.xml");
        final var configuration = new Configuration();
        if (siteConfiguration.exists())
        {
            // try to load site configuration from file
            configuration.addResource(siteConfiguration.openForReading());
        }
        else
        {
            fail("Site configuration resource $ does not exist", siteConfiguration);
        }

        return configuration;
    }
}
