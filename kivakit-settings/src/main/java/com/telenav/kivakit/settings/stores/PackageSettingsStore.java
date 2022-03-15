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

package com.telenav.kivakit.settings.stores;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.PackagePath;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.resource.Package;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;
import com.telenav.kivakit.settings.Settings;
import com.telenav.kivakit.settings.SettingsObject;
import com.telenav.kivakit.settings.SettingsStore;
import com.telenav.kivakit.settings.lexakai.DiagramSettings;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.UNLOAD;

/**
 * <p>
 * A Java package containing settings objects defined by <i>.properties</i> resources.
 * </p>
 *
 * <p>
 * A {@link PackageSettingsStore} can be created with {@link #of(Listener, Package)} or {@link #of(Listener,
 * PackagePath)}. The specified package should contain a set of settings resources, each of which can be passed to the
 * {@link ObjectSerializer} for the resource extension to deserialize the object. Object serializers are located with
 * the {@link ObjectSerializers} object found in the global {@link Registry}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseResourceSettingsStore
 * @see Settings
 * @see SettingsStore
 * @see SettingsObject
 * @see Package
 * @see PackagePath
 */
@UmlClassDiagram(diagram = DiagramSettings.class)
public class PackageSettingsStore extends BaseResourceSettingsStore
{
    private static final Map<PackagePath, PackageSettingsStore> packages = new HashMap<>();

    /**
     * @param _package The package containing settings resources
     * @return A {@link PackageSettingsStore} for the given {@link Package}
     */
    public static PackageSettingsStore of(Listener listener, Package _package)
    {
        return of(listener, _package.path());
    }

    /**
     * @param path The package path for a package containing settings resources
     * @return A {@link PackageSettingsStore} for the given {@link PackagePath}
     */
    public static PackageSettingsStore of(Listener listener, PackagePath path)
    {
        return listener.listenTo(packages.computeIfAbsent(path, ignored -> new PackageSettingsStore(path)));
    }

    /** The path of this configuration package */
    private final PackagePath path;

    /**
     * @param path The path to the package where the configurations are stored
     */
    protected PackageSettingsStore(PackagePath path)
    {
        this.path = path;
    }

    @Override
    public Set<AccessMode> accessModes()
    {
        return Set.of(INDEX, DELETE, UNLOAD, LOAD);
    }

    @Override
    public String name()
    {
        return "[PackageSettingsStore package = " + path + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public Set<SettingsObject> onLoad()
    {
        var _package = Package.packageFrom(path);
        trace("Loading settings objects from $", _package);

        var objects = new ObjectSet<SettingsObject>();

        // Go through files in the package,
        for (var resource : _package.resources())
        {
            // and load any settings object.
            var object = read(resource);
            if (object != null)
            {
                objects.add(object);
            }
        }

        trace("Loaded $ settings objects", objects.size());
        return objects;
    }

    @Override
    public boolean onSave(SettingsObject object)
    {
        return unsupported();
    }

    @Override
    protected boolean onDelete(SettingsObject object)
    {
        return unsupported();
    }
}
