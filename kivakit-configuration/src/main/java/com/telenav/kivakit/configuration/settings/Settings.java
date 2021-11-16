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

package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.configuration.settings.stores.memory.MemorySettingsStore;
import com.telenav.kivakit.configuration.settings.stores.resource.FolderSettingsStore;
import com.telenav.kivakit.configuration.settings.stores.resource.PackageSettingsStore;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * <p>
 * A lookup registry of user-defined settings {@link Object}s.
 * </p>
 *
 * <p><b>Settings</b></p>
 *
 * <p>
 * The global {@link Settings} registry is returned by {@link #global()} and it is the default registry returned by
 * {@link #of(Object)}. It is normally the central point of settings registration and lookup for an application. The
 * global settings registry allows settings objects to be easily queried from client code anywhere. Convenient access to
 * the global settings registry is provided by {@link SettingsTrait}, and by the <i>Component</i> class in
 * <i>kivakit-component</i>. A component can have its own settings registry (not a common use case) by overriding the
 * {@link SettingsTrait#settingsRegistry()} method (which returns the global settings registry by default).
 * </p>
 *
 * <p><b>How Settings are Registered</b></p>
 *
 * <p>
 * A component can easily register settings objects with the {@link SettingsTrait} <i>registerSettings*()</i> methods.
 * In particular, they can be added from a {@link SettingsStore} with {@link #registerSettingsIn(SettingsStore)}. A
 * typical way for a component to add a store of settings is something like:
 * </p>
 * <pre>
 * registerSettingsIn(PackageSettingsStore.of(package));</pre>
 *
 * <p>
 * Settings that are loaded from stores or explicitly added with registration methods are indexed in the store under
 * multiple keys:
 * <ol>
 *     <li>The settings class</li>
 *     <li>The settings class interfaces</li>
 *     <li>All superclasses</li>
 *     <li>All superinterfaces</li>
 * </ol>
 *
 * <p>
 * This makes it possible to look up an interface implementation by specifying looking up the interface class.
 * For example:
 * <pre>
 * var driver = requireSettings(DatabaseDriverSettings.class);</pre>
 * might return an instance of <i>QuantumDatabaseDriverSettings.</i> This is the same pattern used in the global
 * object {@link Registry}, to which settings objects are also added.
 * </p>
 *
 * <p><b>The Easiest Way to Register Settings</b></p>
 *
 * <p>
 * Note that the easiest way to register settings is to use the Application class' built-in <i>-deployment</i> switch.
 * This switch is present by default (although it can be turned off by overriding Application.ignoreDeployments()) and
 * will automatically load and register settings using the names of the sub-packages present in the
 * <i>deployments</i> package next to your application class.
 * </p>
 *
 * <p><b>How Settings are Looked Up</b></p>
 *
 * <p>
 * The <i>lookupSettings*()</i> and <i>requireSettings*()</i> methods provided by {@link SettingsTrait} can be used to locate settings
 * objects from any Component (see the <i>kivakit-component</i> project). Settings are located according to this
 * series of steps:
 * <ol>
 *     <li>Look for the object in the resources in the (comma separated) sequence of folders specified by KIVAKIT_SETTINGS_FOLDERS</i>
 *     <li>Look for the object in the global lookup {@link Registry}</li>
 *     <li>Look for the object in the (usually global) {@link Settings}</li>
 * </ol>
 * This sequence allows the command line, and explicit registration of an object, to override any settings loaded from
 * deployments or other settings stores. Note that if you use <i>require(Class)</i> instead of <i>requireSettings(Class)</i>,
 * the specified class will be looked up and found in the global {@link Registry}, and the steps above will not be followed.
 * This means that it will not be possible to override the location of settings from the command line. The same applies
 * to the <i>lookup(Class)</i> and <i>lookupSettings(Class)</i> methods.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see SettingsTrait
 * @see Registry
 * @see Deployment
 * @see MemorySettingsStore
 * @see FolderSettingsStore
 * @see PackageSettingsStore
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class Settings extends MemorySettingsStore implements SettingsTrait
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** The global settings */
    private static final Lazy<Settings> global = Lazy.of(() ->
            LOGGER.listenTo(new Settings()
            {
                @Override
                public String name()
                {
                    return "[Global Settings]";
                }
            }));

    /**
     * @return The global settings object
     */
    public static Settings global()
    {
        return global.get();
    }

    /**
     * @return The settings registry for the given object (the global settings registry by default)
     */
    public static synchronized Settings of(Object object)
    {
        return global();
    }

    /**
     * @return The settings object of the requested type from the global {@link Registry} or from the package of default
     * settings if it is not found there.
     */
    @Override
    public <T> T lookupSettings(Class<T> type,
                                InstanceIdentifier instance,
                                PackagePath defaultSettingsPackage)
    {
        // First load any settings overrides from KIVAKIT_SETTINGS_FOLDERS,
        loadSystemPropertyOverrides();

        // then look in the global object registry for the settings object,
        var settings = Registry.of(this).lookup(type, instance);

        // and if settings still have not been explicitly defined,
        if (settings == null)
        {
            // then search settings objects in this store,
            settings = lookup(new SettingsObject.Identifier(type, instance));

            // and if the settings are still not found and a default settings package was specified,
            if (settings == null && defaultSettingsPackage != null)
            {
                // then load any default settings from the specified package
                trace("Loading default settings from $", defaultSettingsPackage);
                var store = PackageSettingsStore.of(this, defaultSettingsPackage);
                registerSettingsIn(store);
                settings = store.lookup(new SettingsObject.Identifier(type, instance));
            }
        }

        return settings;
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    @Override
    public <T> T lookupSettings(Class<T> type, InstanceIdentifier instance)
    {
        return lookupSettings(type, instance, null);
    }

    @Override
    public Settings registerSettingsIn(SettingsStore settings)
    {
        addAll(settings);
        return this;
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    @Override
    public synchronized Settings registerSettingsObject(Object settings, InstanceIdentifier instance)
    {
        // If a client tries to register a deployment or other settings store this way,
        if (settings instanceof SettingsStore)
        {
            // then tell them to call registerSettingsIn(),
            return fail("To register a Deployment or other SettingsStore, call registerSettingsIn(SettingsStore)");
        }

        add(new SettingsObject(settings.getClass(), instance, settings));

        return this;
    }

    /**
     * Loads settings from the list of folders specified by the KIVAKIT_SETTINGS_FOLDERS environment variable
     */
    private void loadSystemPropertyOverrides()
    {
        // Go through each path specified by the KIVAKIT_SETTINGS_FOLDERS environment variable
        var settingsFolders = OperatingSystem.get().property("KIVAKIT_SETTINGS_FOLDERS");
        if (settingsFolders != null)
        {
            for (var path : settingsFolders.split(",\\s*"))
            {
                // and install
                var folder = Folder.parse(this, path);
                if (folder != null)
                {
                    addAll(FolderSettingsStore.of(this, folder));
                }
                else
                {
                    throw new IllegalStateException("Invalid folder in KIVAKIT_SETTINGS_FOLDERS: " + path);
                }
            }
        }
    }
}
