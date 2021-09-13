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
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.configuration.settings.deployment.DeploymentSet;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.kernel.language.threading.locks.ReadWriteLock;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * <p>
 * A registry of (untyped) user-defined settings {@link Object}s.
 * </p>
 *
 * <p>
 * This class is the base class for {@link SettingsFolder} and {@link SettingsPackage}, which load settings information
 * from <i>.properties</i> files in folders and packages, respectively. Settings objects are loaded when they are
 * requested, and each <i>.properties</i> resource describes <i>just one settings object</i>. This means that a settings
 * folder or package will often have more than one <i>.properties</i> resource in it, such as:
 * </p>
 *
 * <pre>
 * settings
 *  |── DatabaseSettings.properties
 *  ├── WebSettings.properties
 *  └── HdfsSettings.properties
 * </pre>
 *
 * <p>
 * This class is also the base class for {@link Deployment}, which holds settings objects relevant to a particular
 * application or server deployment. Deployments are the most common use case for settings registries and typically they
 * should be all that is required by an application. See {@link Deployment} for detailed usage examples.
 * </p>
 *
 * <p><b>Global Settings</b></p>
 *
 * <p>
 * The global {@link Settings} registry is the default registry returned by {@link #of(Object)}, and it is normally the
 * central point of registration for an application. The global settings registry allows settings objects to be easily
 * queried from client code anywhere. Several static methods are provided for convenience, and in most cases these
 * methods should be sufficient:
 * </p>
 *
 * <ul>
 *     <li>{@link #register(Object)} - Registers the given object in the global settings registry</li>
 *     <li>{@link #registerAllIn(Folder)} - Registers the objects defined by .properties files in the given folder in the global settings registry</li>
 *     <li>{@link #registerAllIn(Package)} - Registers the objects defined by .properties files in the given package in the global settings registry</li>
 *     <li>{@link #require(Class)} - Returns the settings object of the given class from the global settings registry</li>
 *     <li>{@link #require(Class, Enum)} - Returns the settings object of the given class and type from the global settings registry</li>
 *     <li>{@link #require(Class, Enum)} - Returns the settings object of the given class and type from the global settings registry</li>
 * </ul>
 *
 * <p>
 * A component can also have its own settings registry. This can be retrieved with {@link Settings#of(Object)}, which
 * returns the global registry by default. The <i>Application</i> object uses {@link Settings#of(Object)} by default to
 * access application's settings registry.
 * </p>
 *
 * <p><b>How Settings Are Located</b></p>
 *
 * <p>
 * The {@link #require(Class)} method and related overloads find settings objects. To locate a settings object,
 * this method first looks for the given settings object using the global {@link Registry} (all settings objects are
 * registered in this lookup registry). This allows settings to be overridden with a {@link Deployment}s or using a
 * command line variable, as described below. If the required settings object is not already registered, all settings
 * objects are loaded from the specified package to provide a default object. Then, the lookup is retried and the
 * result is returned.
 * </p>
 *
 * <p><b>Overriding Settings from the Command Line</b></p>
 *
 * <p>
 * Settings can be overridden from the command line by specifying a comma-separated folder list using
 * KIVAKIT_SETTINGS_FOLDERS environment variable. All settings from each folder in the list will be loaded and will
 * override the default settings that might otherwise be used.
 * </p>
 *
 * <pre>
 * java -DKIVAKIT_SETTINGS_FOLDERS=my-settings-folder [...]
 * </pre>
 *
 * <p><b>Locating Configurations with the Global Registry</b></p>
 *
 * <p>
 * For convenience, each settings object is added to the global lookup. This allows clients to easily look up
 * settings objects and not depend on where they came from. For example:
 * </p>
 * <pre>
 * var serverSettings = Registry.lookup(ServerSettings.class)
 * </pre>
 *
 * <p><b>Properties File Format</b></p>
 *
 * <p>
 * Properties files are of this general form:
 * </p>
 *
 * <p><i>Server1.properties</i></p>
 *
 * <pre>
 * class=com.telenav.navigation.my.application.Server$Configuration
 * instance=SERVER1
 * port=aws.amazon.com:7001
 * </pre>
 *
 * <p>
 * Here, the "class" key designates a class to instantiate (note that the nested class has to be indicated with '$'
 * rather than '.' here). The object that is created from this class is populated with the property values by using
 * {@link ObjectPopulator}, which automatically converts each property value into an object using the converter
 * framework. To do this, properties in the settings object are tagged with {@link KivaKitPropertyConverter}
 * indicating which converter the {@link ObjectPopulator} should use to convert a string value in the properties file to
 * the corresponding object. For example, in this case the property converter for the settings class above is
 * Port.Converter and the port property is converted to a Port object:
 * </p>
 *
 * <p><i>Server.Configuration Class</i></p>
 *
 * <pre>
 * public class Configuration
 * {
 *     private Port port;
 *
 *    {@literal @}KivaKitPropertyConverter(Port.Converter.class)
 *     public void port(final Port port)
 *     {
 *         this.port = port;
 *     }
 *
 *    [...]
 * }
 * </pre>
 *
 * <p>
 * The key "instance" designates which instance of settings object the *.properties* file refers to
 * (in the event that more than one object of the same type is registered with the same registry).
 * </p>
 *
 * <p><b>Settings Registry Instances</b></p>
 *
 * <p>
 * The methods above under <i>Global Settings</i> will typically be enough for most applications. However, for more
 * complex requirements, it is possible to work with individual settings registry instances. Settings objects can be
 * registered with a {@link Settings} registry instance using these methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #register(Object)} - Registers the given user-defined settings object</li>
 *     <li>{@link #register(Object, Enum)} - Registers the given instance of the given user-defined settings object</li>
 *     <li>{@link #register(Object, InstanceIdentifier)} - Registers the given instance of the given user-defined settings object</li>
 *     <li>{@link #registerAll(Collection)} - Adds the collection of settings objects to this settings registry</li>
 *     <li>{@link #registerAll(Settings)} - Adds the objects in the given settings registry to this registry</li>
 *     <li>{@link #registerAllIn(Folder)} - Registers all the settings objects defined by .properties files in the given folder</li>
 *     <li>{@link #registerAllIn(Package)} - Registers all the settings objects defined by .properties files in the given package</li>
 *     <li>{@link #registerAllIn(Class, String)} - Adds the package of .properties files at the given path relative to the given class</li>
 *     <li>{@link #registerAllIn(Folder)} - Adds the folder of .properties files</li>
 *     <li>{@link #install()} - Installs the contents of this {@link Settings} into the global settings registry</li>
 * </ul>
 * <p>
 * Settings from a settings registry instance can be added to the global settings registry with {@link Settings#install()}.
 * The install method is used in {@link Deployment} to install the settings objects for a server deployment in the
 * global settings registry. See {@link Deployment} for a detailed example of how this works.
 * </p>
 *
 * <p>
 * Configuration objects are loaded transparently by using the {@link Deployment}, {@link SettingsFolder}
 * and {@link SettingsPackage} subclasses of this class. For example:
 * </p>
 *
 * <pre>
 * ConfigurationFolder.of(new Folder("development")).forEach(System.out::println);
 * </pre>
 *
 * <p>
 * Settings objects can then be queried by class, and with an optional {@link Enum} or {@link InstanceIdentifier}
 * specifier in cases where more than one settings object of a given class is in use. The <i>settings()</i> overloaded
 * methods return a settings object or null if no settings object can be found. The <i>requireSettings()</i> methods throw
 * an exception if no settings object can be found for the given arguments:
 * </p>
 *
 * <ul>
 *     <li>{@link #has(Class)} - Determines if a settings object of the given type exists</li>
 *     <li>{@link #has(Class, InstanceIdentifier)} - Determines if the specified instance of given settings object type exists</li>
 *     <li>{@link #settings()} - All settings objects</li>
 *     <li>{@link #settings(Class)} - Gets the settings object of the given type</li>
 *     <li>{@link #settings(Class, InstanceIdentifier)} - Gets the specified instance of the settings object with the given type</li>
 *     <li>{@link #settings(Class, Enum)} - Gets the specified instance of the settings object with the given type</li>
 *     <li>{@link #require(Class)} - Gets the specified settings object or fails with {@link Ensure#fail()}</li>
 *     <li>{@link #require(Class, InstanceIdentifier)} - Gets the specified settings object instance or fails with {@link Ensure#fail()}</li>
 *     <li>{@link #require(Class, Enum)} - Gets the specified settings object instance or fails with {@link Ensure#fail()}</li>
 * </ul>
 *
 * <p><b>Loading Configurations as DeploymentSets</b></p>
 *
 * <p>
 * In most cases, it will not be necessary to manually add settings objects to a settings registry. They can be
 * automatically loaded and added by using the {@link Deployment} and {@link DeploymentSet} classes and occasionally
 * with {@link SettingsPackage} and {@link SettingsFolder}. To understand how to load and use collections of settings
 * objects called deployments in an Application, see {@link Deployment}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see SettingsFolder
 * @see SettingsPackage
 */
@SuppressWarnings("ClassEscapesDefinedScope")
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class Settings extends BaseRepeater implements Named, Iterable<Object>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    /** The global settings */
    private static final Lazy<Settings> global = Lazy.of(() ->
            LOGGER.listenTo(new Settings()
            {
                @Override
                public String name()
                {
                    return "[Global]";
                }
            }));

    /**
     * @return The settings registry for the given object (the global settings registry by default)
     */
    public static synchronized Settings of(final Object object)
    {
        return global.get();
    }

    /** Map to get settings entries by identifier */
    private final Map<Entry.Identifier, Entry> entries = new HashMap<>();

    /** True if the settings in this registry are loaded */
    private boolean loaded;

    /** Lock for accessing settings entries */
    private final ReadWriteLock lock = new ReadWriteLock();

    /** True if this registry has been installed with install() */
    private boolean installed;

    /**
     * Clears this set of settings objects
     */
    public void clear()
    {
        lock.write(entries::clear);
    }

    /**
     * @return True if this set has a settings object of the given type
     */
    public boolean has(final Class<?> type)
    {
        return settings(type) != null;
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    public boolean has(final Class<?> type, final InstanceIdentifier instance)
    {
        return settings(type, instance) != null;
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    public boolean has(final Class<?> type, final Enum<?> instance)
    {
        return has(type, InstanceIdentifier.of(instance));
    }

    /**
     * Installs the settings objects in this registry into the global {@link Settings} registry
     */
    public Settings install()
    {
        return registerAllWith(global.get());
    }

    /**
     * @return An iterator over the underlying settings {@link Object}s in this set (i.e., not the {@link Entry}
     * objects)
     */
    @NotNull
    @Override
    @UmlExcludeMember
    public Iterator<Object> iterator()
    {
        return asSet()
                .stream()
                .map(Entry::object)
                .collect(Collectors.toSet())
                .iterator();
    }

    /**
     * @return Add the given settings object to this set
     */
    public Settings register(final Object settings)
    {
        return register(settings, InstanceIdentifier.SINGLETON);
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    public Settings register(final Object settings, final Enum<?> instance)
    {
        return register(settings, InstanceIdentifier.of(instance));
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    public synchronized Settings register(final Object settings, final InstanceIdentifier instance)
    {
        // If a client tries to register a deployment this way,
        if (settings instanceof Deployment)
        {
            // then tell them to call registerAll(),
            return fail("To register a Deployment, call registerAll()");
        }
        // and if they try to register a Settings object this way,
        else if (settings instanceof Settings)
        {
            // tell them to call registerAll().
            return fail("To add a Settings object, call addAll()");
        }

        internalAdd(new Entry(new Entry.Identifier(settings.getClass(), instance), settings));

        return this;
    }

    public Settings registerAll(final Settings settings)
    {
        internalAddAll(settings);
        return this;
    }

    public Settings registerAll(final Collection<Object> settings)
    {
        settings.forEach(this::register);
        return this;
    }

    public Settings registerAllIn(final Folder folder)
    {
        registerAll(listenTo(new SettingsFolder(folder)));
        return this;
    }

    public Settings registerAllIn(final PackagePath path)
    {
        registerAll(listenTo(SettingsPackage.of(path)));
        return this;
    }

    public Settings registerAllIn(final Package package_)
    {
        registerAll(listenTo(SettingsPackage.of(package_)));
        return this;
    }

    public Settings registerAllIn(final Class<?> relativeTo, final String path)
    {
        registerAllIn(PackagePath.parsePackagePath(relativeTo, path));
        return this;
    }

    public Settings registerAllIn(final Class<?> type)
    {
        registerAllIn(PackagePath.packagePath(type));
        return this;
    }

    /**
     * Installs the settings objects in this registry into the given {@link Settings} registry
     */
    public Settings registerAllWith(final Settings settings)
    {
        if (!installed)
        {
            installed = true;
            trace("Installing settings from $ into $", name(), settings.name());
            settings.registerAll(this);
        }
        return this;
    }

    /**
     * @return The specified instance of the given settings type, or {@link Ensure#fail()} is called if no settings
     * object of the given type and instance can be found.
     */
    public synchronized <T> T require(final Class<T> type, final InstanceIdentifier instance)
    {
        return require(type, PackagePath.packagePath(type), instance);
    }

    /**
     * @return The settings object of the given type or {@link Ensure#fail()} is called if no configuration of the given
     * type can be found.
     */
    public <T> T require(final Class<T> type)
    {
        return require(type, InstanceIdentifier.SINGLETON);
    }

    /**
     * @return The specified instance of the given settings type, or {@link Ensure#fail()} is called if no settings
     * object of the given type and instance can be found.
     */
    public <T> T require(final Class<T> type, final Enum<?> instance)
    {
        return require(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return All settings objects in this registry
     */
    public Iterator<Object> settings()
    {
        return iterator();
    }

    /**
     * @return The settings object of the given type
     */
    @UmlRelation(label = "gets values")
    public <T> T settings(final Class<T> type)
    {
        return settings(new Entry.Identifier(type));
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    public <T> T settings(final Class<T> type, final InstanceIdentifier instance)
    {
        return settings(new Entry.Identifier(type, instance));
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    public <T> T settings(final Class<T> type, final Enum<?> instance)
    {
        return settings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    public <T> T settings(final Class<T> type, final String instance)
    {
        return settings(type, InstanceIdentifier.of(instance));
    }

    @Override
    public String toString()
    {
        final var strings = new StringList();
        for (final var at : asSet())
        {
            strings.add(at.toString());
        }
        return strings.join("\n");
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Adds the given settings entry to this registry
     * </p>
     */
    @UmlExcludeMember
    protected void internalAdd(final Entry entry)
    {
        assert entry != null;

        // Obtain a write lock,
        lock.write(() ->
        {
            // add the entry to the global lookup registry
            registry().register(entry.object(), entry.identifier().instance());

            // then walk up the class hierarchy of the configuration object,
            final var instance = entry.identifier().instance();
            for (var at = (Class<?>) entry.object().getClass(); !at.equals(Object.class); at = at.getSuperclass())
            {
                // adding the configuration object for each superclass.
                entries.put(new Entry.Identifier(at, instance), entry);
            }
        });
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Adds each settings object in the given registry to this registry
     * </p>
     */
    @UmlExcludeMember
    protected void internalAddAll(final Settings that)
    {
        lock.write(() -> that.asSet().forEach(this::internalAdd));
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Loads a configuration from the given properties resource. Note that when this method is called by a subclass
     * implementing {@link #onLoad()}, it will already hold a write lock.
     *
     * @return A configuration loaded from the given properties resource (could be a file in a folder or a resource in a
     * package)
     */
    @UmlExcludeMember
    protected Entry internalLoadConfiguration(final Resource resource)
    {
        // Load the given properties
        trace("Loading configuration from $", resource);
        final var properties = PropertyMap.load(this, resource);
        try
        {
            // then get the configuration class to instantiate,
            final var configurationClassName = properties.get("class");
            ensureNotNull(configurationClassName, "Missing class property in $", resource);
            final var configurationClass = Class.forName(configurationClassName);
            ensureNotNull(configurationClass, "Unable to load class $ specified in $", configurationClass, resource);
            trace("Configuration class: $", configurationClass.getSimpleName());

            // and the name of which identifier of the class to configure (if any)
            final var configurationInstance = properties.get("instance");
            final var identifier = configurationInstance != null ? InstanceIdentifier.of(configurationInstance) : InstanceIdentifier.SINGLETON;
            trace("Configuration identifier: $", identifier);

            // then create the configuration object and populate it using the converter framework
            final var configuration = properties.asObject(this, configurationClass);
            if (configuration != null)
            {
                trace("Loaded configuration: $", configuration);

                // and return the configuration set entry for the fully loaded configuration object
                return new Entry(new Entry.Identifier(configurationClass, identifier), configuration);
            }
            else
            {
                return fail("Unable to load configuration object from $", resource);
            }
        }
        catch (final Exception e)
        {
            return fail(e, "Unable to load properties from $", resource);
        }
    }

    /**
     * @return The set of loaded settings entries
     */
    @UmlExcludeMember
    protected Set<Entry> onLoad()
    {
        return Set.of();
    }

    /** Gets a <b>copy</b> of the {@link Entry} objects in this set, loading them if need be */
    private Set<Entry> asSet()
    {
        return lock.write(() ->
        {
            load();
            return Sets.hashset(entries.values());
        });
    }

    /** Loads configurations if not already loaded */
    private void load()
    {
        if (!loaded)
        {
            trace("Loading configurations from $", name());
            lock.write(() ->
            {
                final var entries = onLoad();
                entries.forEach(this::internalAdd);
                loaded = true;
            });
        }
    }

    /**
     * Loads settings from the list of folders specified by the KIVAKIT_SETTINGS_FOLDERS environment variable
     */
    private void loadSystemPropertyOverrides()
    {
        // Go through each path specified by the KIVAKIT_SETTINGS_FOLDERS environment variable
        final var settingsFolders = OperatingSystem.get().property("KIVAKIT_SETTINGS_FOLDERS");
        if (settingsFolders != null)
        {
            for (final var path : settingsFolders.split(",\\s*"))
            {
                // and install
                final var folder = Folder.parse(path);
                if (folder != null)
                {
                    LOGGER.listenTo(new SettingsFolder(folder)).install();
                }
                else
                {
                    throw new IllegalStateException("Invalid folder in KIVAKIT_SETTINGS_FOLDERS: " + path);
                }
            }
        }
    }

    private Registry registry()
    {
        return Registry.of(this);
    }

    /**
     * @return The settings object of the requested type from the global {@link Registry} or from the package of default
     * settings if it is not found there.
     */
    private <T> T require(final Class<T> settingsClass,
                          final PackagePath defaultSettingsPackage,
                          final InstanceIdentifier identifier)
    {
        // Load any settings overrides from KIVAKIT_SETTINGS_FOLDERS
        loadSystemPropertyOverrides();

        // then look in the global lookup for the settings
        var settings = registry().lookup(settingsClass, identifier);

        // If settings still have not been defined
        if (settings == null)
        {
            // then load the default settings
            DEBUG.trace("Installing default settings from $", defaultSettingsPackage);
            final var defaultSettings = LOGGER.listenTo(SettingsPackage.of(defaultSettingsPackage));
            defaultSettings.install();

            // and try again,
            settings = registry().lookup(settingsClass);

            // and finally, fail if the settings still cannot be found
            Ensure.ensureNotNull(settings, "Unable to locate settings: ${class}", settingsClass);
        }

        return settings;
    }

    /**
     * @return The configuration for the given identifier
     */
    private <T> T settings(final Entry.Identifier identifier)
    {
        return lock.read(() ->
        {
            final var settings = entries.get(identifier);
            return settings == null ? null : settings.object();
        });
    }
}
