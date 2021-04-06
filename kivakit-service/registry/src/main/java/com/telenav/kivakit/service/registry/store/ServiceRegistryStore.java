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

package com.telenav.kivakit.service.registry.store;

import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.language.primitives.Booleans;
import com.telenav.kivakit.core.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.core.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.service.registry.ServiceRegistry;
import com.telenav.kivakit.service.registry.ServiceRegistrySettings;
import com.telenav.kivakit.service.registry.project.ServiceRegistryProject;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRegistry;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import static com.telenav.kivakit.core.serialization.core.SerializationSession.Type.RESOURCE;

/**
 * <b>Not public API</b>
 * <p>
 * This store saves registry information with {@link #save(ServiceRegistry)}, ensuring that port assignments and other
 * information can be reloaded with {@link #load(Class)} when a service registry is down for a short time such as during
 * and upgrade or reboot. Note that service registration is only preserved for {@link
 * ServiceRegistrySettings#serviceRegistryStoreExpirationTime()}. If a reboot takes longer than this, the serialized
 * registry data is assumed to be too out-of-date to be useful.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlNotPublicApi
public class ServiceRegistryStore extends BaseRepeater
{
    /**
     * Loads the service registry of the given type from the cache folder where that type of registry is saved by {@link
     * #save(ServiceRegistry)}
     */
    @UmlRelation(label = "loads")
    public synchronized ServiceRegistry load(final Class<? extends ServiceRegistry> type)
    {
        // Get the serialization file for the service registry type (this allows us to run both
        // local and network registries on the same machine)
        final var file = file(type);
        if (file.exists())
        {
            // and if the data is not too old
            final var lastModified = file.lastModified();
            final var expirationTime = settings().serviceRegistryStoreExpirationTime();
            if (lastModified.elapsedSince().isLessThan(expirationTime))
            {
                // then open the file
                trace("Loading service registry from $", file);
                try (final var input = file.openForReading())
                {
                    // create a serialization object and read the serialized registry
                    final var session = ServiceRegistryProject.get().sessionFactory().session(this);
                    session.open(RESOURCE, settings().version(), input);
                    final VersionedObject<ServiceRegistry> object = session.read();

                    // then unregister the loaded class with the Debug class so the debug flag
                    // is re-considered for the newly loaded instance
                    Debug.unregister(object.get().getClass());

                    // and add the listener to the registry.
                    trace("Loaded service registry");
                    return listenTo(object.get());
                }
                catch (final Exception e)
                {
                    // We are unable to load the service registry, so remove the file.
                    file.delete();
                }
            }
            else
            {
                // The file is too old so remove it.
                file.delete();
            }
        }
        return null;
    }

    /**
     * Saves the given registry to a cache folder
     */
    public synchronized void save(final ServiceRegistry registry)
    {
        if (Booleans.isTrue(JavaVirtualMachine.property("KIVAKIT_SAVE_REGISTRY", "true")))
        {
            final var file = file(registry.getClass()).withExtension(Extension.TMP);
            trace("Saving service registry to $", file.parent());
            if (file.delete())
            {
                try (final var output = file.openForWriting())
                {
                    final var session = ServiceRegistryProject.get().sessionFactory().session(this);
                    session.open(RESOURCE, settings().version(), output);
                    session.write(new VersionedObject<>(settings().version(), registry));
                    session.close();
                }
                catch (final Exception e)
                {
                    problem(e, "Unable to save service registry to $", file);
                }
                file.renameTo(file(registry.getClass()));
            }
            trace("Service registry saved");
        }
    }

    private File file(final Class<? extends ServiceRegistry> type)
    {
        return Folder.kivakitCacheFolder()
                .folder("service-registry")
                .mkdirs()
                .file(CaseFormat.camelCaseToHyphenated(type.getSimpleName()) + ".kryo");
    }

    private ServiceRegistrySettings settings()
    {
        return Settings.require(ServiceRegistrySettings.class);
    }
}
