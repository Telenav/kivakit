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

package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.network.core.Port;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.resources.jar.launcher.JarLauncher;
import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
import com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.ServiceMetadata;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.client.ServiceRegistryClient;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static com.telenav.kivakit.core.resource.resources.jar.launcher.JarLauncher.ProcessType.CHILD;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlClassDiagram(diagram = DiagramHdfs.class)
@UmlRelation(label = "configures with", referent = HdfsSettings.class)
public class HdfsProxyClient extends BaseRepeater
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Lazy<HdfsProxyClient> singleton = Lazy.of(() -> LOGGER.listenTo(new HdfsProxyClient()));

    public static HdfsProxyClient get()
    {
        return singleton.get();
    }

    @UmlAggregation
    private HdfsProxy proxy;

    @UmlAggregation(label = "data access")
    private Port dataPort;

    @UmlAggregation(label = "RMI")
    private Port rmiObjectPort;

    private HdfsProxyClient()
    {
        listenTo(this);
    }

    public Port dataPort()
    {
        return dataPort;
    }

    public Folder logFolder()
    {
        return Folder.kivakitTemporaryFolder().folder(new FileName("kivakit-hdfs-proxy")).mkdirs();
    }

    public HdfsProxy proxy()
    {
        // If we haven't found the proxy yet,
        if (proxy == null)
        {
            // but the proxy is already running
            final var existing = lookup();
            if (existing != null)
            {
                // return it
                proxy = existing;
            }
            else
            {
                // otherwise launch the proxy
                launchProxy();

                // and wait until it is ready.
                proxy = waitForProxy();
            }
        }
        return proxy;
    }

    public Port rmiObjectPort()
    {
        return rmiObjectPort;
    }

    private void launchProxy()
    {
        // Find a free port for the child proxy server process to use
        final var client = listenTo(new ServiceRegistryClient());

        final var settings = Settings.require(HdfsSettings.class);

        final var materialized = settings.configurationFolder().materialize();

        final var metadata = new ServiceMetadata()
                .version(HdfsProxy.VERSION)
                .contactEmail(settings.contactEmail());

        final var rmiObjectService = client.register
                (
                        Scope.localhost(),
                        new ServiceType("com-telenav-kivakit-hdfs-proxy-rmi"),
                        metadata.description("RMI remote object that provides a subset of the HDFS API to HdfsProxyClient")
                );

        final var dataService = client.register
                (
                        Scope.localhost(),
                        new ServiceType("com-telenav-kivakit-hdfs-proxy-data"),
                        metadata.description("A socket-based proxy that performs I/O to HDFS for HdfsProxyClient")
                );

        if (rmiObjectService.has() && dataService.has())
        {
            rmiObjectPort = rmiObjectService.get().port();
            dataPort = dataService.get().port();

            // download the jar (if need be) and launch it as a child process using the ports
            // allocated for the current application / process.
            final var local = Folder.kivakitHome()
                    .folder("kivakit-filesystems/hdfs-proxy/target")
                    .file("kivakit-hdfs-proxy-" + KivaKit.get().version() + ".jar");
            final var process = listenTo(new JarLauncher())
                    .source(local)
                    .source(settings.proxyJar())
                    .arguments(
                            "-rmi-object-port=" + rmiObjectPort.number(),
                            "-data-port=" + dataPort.number(),
                            "-configuration-folder=" + materialized,
                            "-username=" + settings.username())
                    .processType(CHILD)
                    .run();

            new KivaKitShutdownHook(KivaKitShutdownHook.Order.LAST, process::destroyForcibly);
        }
        else
        {
            problem("Unable to register HDFS services to launch proxy");
        }
    }

    private HdfsProxy lookup()
    {
        try
        {
            final var rmiObjectName = HdfsProxy.RMI_REGISTRY_NAME + "-" + rmiObjectPort().number();
            final Registry registry = LocateRegistry.getRegistry("localhost", HdfsProxy.RMI_REGISTRY_PORT);
            return (HdfsProxy) registry.lookup(rmiObjectName);
        }
        catch (final Exception e)
        {
            if (isDebugOn())
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    private HdfsProxy waitForProxy()
    {
        var start = Time.now();
        var wait = Duration.seconds(1);
        var attempts = 0;

        // Loop forever
        while (true)
        {
            // or until the proxy is ready
            final HdfsProxy proxy = lookup();
            if (proxy != null)
            {
                trace("Found HDFS proxy");
                return proxy;
            }

            // and every so often if we've been trying for a while
            if (start.elapsedSince().isGreaterThan(Duration.minutes(2)))
            {
                // try again to launch the jar
                launchProxy();
                start = Time.now();
            }

            // pausing briefly between tries
            warning("Unable to find HDFS proxy. Will try again in $.", wait);
            wait.sleep();
            if (attempts++ > 8)
            {
                wait = Duration.seconds(5);
            }
        }
    }
}
