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

package com.telenav.kivakit.configuration;

import com.telenav.kivakit.configuration.deployment.Deployment;
import com.telenav.kivakit.configuration.deployment.DeploymentSet;
import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.kernel.language.threading.locks.ReadWriteLock;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * A mutable set of (untyped) configuration {@link Object}s that can be loaded from a package or folder. Configuration
 * objects can be queried by class, and with an optional instance identifier in cases where more than one configuration
 * object of a given class is in use.
 *
 * <p><b>The Global Configuration Set</b></p>
 *
 * <p>
 * Configuration information is generally loaded into the global configuration set, which allows configuration objects
 * to be easily queried from client code anywhere. Configuration sets can be added to the global set with {@link
 * #install()}. This method is used in {@link Deployment} to install the configuration objects for a server deployment
 * in the global set. See {@link Deployment} for a detailed example of how this works.
 * </p>
 *
 * <p>
 * This class is the base class for {@link Deployment}, which holds configuration objects relevant to a particular
 * server deployment, as well as {@link ConfigurationFolder} and {@link ConfigurationPackage}, which load configuration
 * information from folders and packages, respectively. Configurations objects are loaded automatically when they are
 * requested and each .properties resource describes <i>just one configuration object</i>. This means that a
 * configuration folder or package will often have more than one .properties resource in it.
 * </p>
 *
 * <p><b>Locating Configurations with Registry</b></p>
 *
 * <p>
 * For convenience, each configuration object is added to the global lookup. This allows clients to easily look up
 * configuration objects and not depend on where they came from. For example:
 * </p>
 * <pre>
 *     var serverSettings = Registry.global().locate(ServerSettings.class)
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
 * configuration-instance=SERVER1
 * port=aws.amazon.com:7001
 * </pre>
 *
 * <p>
 * Here, the "class" key designates a class to instantiate (note that the nested class has to be indicated with '$'
 * rather than '.' here). The object that is created from this class is populated with the property values by using
 * {@link ObjectPopulator}, which automatically converts each property value into an object using the converter
 * framework. To do this, properties in the configuration object are tagged with {@link KivaKitPropertyConverter}
 * indicating which converter the {@link ObjectPopulator} should use to convert a string value in the properties file to
 * the corresponding object. For example, in this case the property converter for the configuration class above is
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
 * The key "configuration-instance" designates which instance of the configuration the properties file is configuring,
 * in the event that more than one object of the same type is being configured.
 * </p>
 *
 * <p><b>Locating Configuration Objects at Runtime</b></p>
 *
 * <p>
 * Once a configuration set (usually the global configuration set, which can be retrieved with {@link #global()}) has
 * been loaded with configuration information, that information can be queried with:
 * </p>
 *
 * <ul>
 *     <li>{@link #has(Class)} - Determines if a configuration object of the given type exists</li>
 *     <li>{@link #has(Class, InstanceIdentifier)} - Determines if the specified instance of given configuration object type exists</li>
 *     <li>{@link #get(Class)} - Gets the configuration object of the given type</li>
 *     <li>{@link #get(Class, InstanceIdentifier)} - Gets the specified instance of the configuration object with the given type</li>
 *     <li>{@link #get(Class, Enum)} - Gets the specified instance of the configuration object with the given type</li>
 *     <li>{@link #require(Class)} - Gets the specified configuration object or fails with {@link Ensure#fail()}</li>
 *     <li>{@link #require(Class, InstanceIdentifier)} - Gets the specified configuration object instance or fails with {@link Ensure#fail()}</li>
 *     <li>{@link #require(Class, Enum)} - Gets the specified configuration object instance or fails with {@link Ensure#fail()}</li>
 * </ul>
 *
 * <p><b>Loading Configurations as DeploymentSets</b></p>
 *
 * <p>
 * <i>The details below are present for flexibility and will not be necessary most of the time.</i> To understand how to load
 * and use collections of configuration objects called deployments in an Application, see {@link Deployment}.
 * </p>
 *
 * <p><b>Adding and Loading Configuration Objects</b></p>
 *
 * <ul>
 *     <li>{@link #addPackage(Class, String)} - Adds the package of .properties files at the given path relative to the given class</li>
 *     <li>{@link #addFolder(Folder)} - Adds the folder of .properties files</li>
 *     <li>{@link #addDeployment(Deployment)} - Merges a deployment into this configuration set</li>
 *     <li>{@link #install()} - Installs the contents of this {@link ConfigurationSet} into the global configuration set</li>
 * </ul>
 * <p>
 * In most cases, it will not be necessary to manually add configuration objects to a set. They can be automatically
 * loaded and added by using the {@link Deployment} and {@link DeploymentSet} classes and occasionally with {@link
 * ConfigurationPackage} and {@link ConfigurationFolder}. When it is necessary, configuration objects can be added
 * with:
 * <ul>
 *     <li>{@link #add(Object)} - Adds the configuration object to this set</li>
 *     <li>{@link #add(Object, InstanceIdentifier)} - Adds the configuration object of the specified instance to this set</li>
 *     <li>{@link #add(Object, Enum)} - Adds the configuration object of the specified instance to this set</li>
 *     <li>{@link #addAll(Collection)} - Adds the collection of configuration objects to this set</li>
 *     <li>{@link #addSet(ConfigurationSet)} - Adds the objects in the given configuration set to this set</li>
 * </ul>
 *
 * <p>
 * Configuration objects are loaded transparently by using the {@link Deployment}, {@link ConfigurationFolder}
 * and {@link ConfigurationPackage} subclasses of this class. For example:
 * </p>
 *
 * <pre>
 * var development = new ConfigurationFolder(new Folder("development"));
 * development.forEach(System.out::println);
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see ConfigurationFolder
 * @see ConfigurationPackage
 */
@SuppressWarnings("ClassEscapesDefinedScope")
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class ConfigurationSet extends BaseRepeater implements Named, Iterable<Object>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static ConfigurationSet global;

    /**
     * @return The global configuration set
     */
    public static synchronized ConfigurationSet global()
    {
        if (global == null)
        {
            LOGGER.listenTo(global = new ConfigurationSet() {});
        }
        return global;
    }

    /** The set of configurations */
    @UmlAggregation
    private final Set<Entry> entries = new HashSet<>();

    /** Map to get configurations by identifier */
    private final Map<Entry.ConfigurationIdentifier, Entry> identifierToConfiguration = new HashMap<>();

    /** True if the configurations in this set are loaded */
    private boolean loaded;

    /** Lock for accessing configurations */
    private final ReadWriteLock lock = new ReadWriteLock();

    /** True if this configuration set has been installed */
    private boolean installed;

    /**
     * @return Add the given configuration object to this set
     */
    public ConfigurationSet add(final Object configuration)
    {
        internalAdd(new Entry(new Entry.ConfigurationIdentifier(configuration.getClass()), configuration));
        return this;
    }

    /**
     * @return Adds the given instance of a configuration object to this set
     */
    public ConfigurationSet add(final Object configuration, final Enum<?> instance)
    {
        return add(configuration, new InstanceIdentifier(instance));
    }

    /**
     * @return Adds the given instance of a configuration object to this set
     */
    public ConfigurationSet add(final Object configuration, final InstanceIdentifier instance)
    {
        // If a client tries to add a deployment this way,
        if (configuration instanceof Deployment)
        {
            // then tell them to call addDeployment(),
            return fail("To add a Deployment, call addDeployment()");
        }
        // and if they try to load a configuration set,
        else if (configuration instanceof ConfigurationSet)
        {
            // tell them to call addSet().
            return fail("To add a ConfigurationSet, call addSet()");
        }

        internalAdd(new Entry(new Entry.ConfigurationIdentifier(configuration.getClass(), instance), configuration));

        return this;
    }

    public ConfigurationSet addAll(final Collection<Object> configurations)
    {
        configurations.forEach(this::add);
        return this;
    }

    public ConfigurationSet addDeployment(final Deployment deployment)
    {
        internalAddAll(deployment);
        return this;
    }

    public ConfigurationSet addFolder(final Folder folder)
    {
        addSet(listenTo(new ConfigurationFolder(folder)));
        return this;
    }

    public ConfigurationSet addPackage(final PackagePath path)
    {
        addSet(listenTo(new ConfigurationPackage(path)));
        return this;
    }

    public ConfigurationSet addPackage(final Class<?> relativeTo, final String path)
    {
        addPackage(PackagePath.parsePackagePath(relativeTo, path));
        return this;
    }

    public ConfigurationSet addPackage(final Class<?> type)
    {
        addPackage(PackagePath.packagePath(type));
        return this;
    }

    public ConfigurationSet addSet(final ConfigurationSet set)
    {
        internalAddAll(set);
        return this;
    }

    /**
     * Clears this set of configurations
     */
    public void clear()
    {
        lock.write(entries::clear);
    }

    public Iterator<Object> configurations()
    {
        return iterator();
    }

    /**
     * @return The configuration object of the given type
     */
    @UmlRelation(label = "gets values")
    public <T> T get(final Class<T> type)
    {
        return get(new Entry.ConfigurationIdentifier(type));
    }

    /**
     * @return The configuration instance of the given type and instance identifier
     */
    public <T> T get(final Class<T> type, final InstanceIdentifier instance)
    {
        return get(new Entry.ConfigurationIdentifier(type, instance));
    }

    /**
     * @return The configuration instance of the given type and instance identifier
     */
    public <T> T get(final Class<T> type, final Enum<?> instance)
    {
        return get(type, new InstanceIdentifier(instance));
    }

    /**
     * @return True if this set has a configuration object of the given type
     */
    public boolean has(final Class<?> type)
    {
        return get(type) != null;
    }

    /**
     * @return True if this set has the specified instance of the configuration object specified by the given type
     */
    public boolean has(final Class<?> type, final InstanceIdentifier instance)
    {
        return get(type, instance) != null;
    }

    /**
     * @return True if this set has the specified instance of the configuration object specified by the given type
     */
    public boolean has(final Class<?> type, final Enum<?> instance)
    {
        return has(type, new InstanceIdentifier(instance));
    }

    /**
     * Installs the configuration objects in this set into the global {@link ConfigurationSet}
     */
    public ConfigurationSet install()
    {
        if (!installed)
        {
            installed = true;
            trace("Installing configurations from $", name());
            global().addSet(this);
        }
        return this;
    }

    /**
     * @return An iterator over the underlying configuration {@link Object}s in this set (i.e., not the {@link Entry}
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
     * @return The specified instance of the given configuration type, or {@link Ensure#fail()}s if no configuration can
     * be found.
     */
    public synchronized <T> T require(final Class<T> type, final InstanceIdentifier instance)
    {
        return ensureNotNull(get(type, instance), "Couldn't find configuration for $:$", type, instance);
    }

    /**
     * @return The configuration object of the given type or failure if no configuration can be found
     */
    public synchronized <T> T require(final Class<T> type)
    {
        return require(type, InstanceIdentifier.SINGLETON);
    }

    /**
     * @return The configuration object of the given type or failure if no configuration can be found
     */
    public synchronized <T> T require(final Class<T> type, final Enum<?> instance)
    {
        return require(type, new InstanceIdentifier(instance));
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

    /** Adds the given configuration to this set */
    @UmlExcludeMember
    protected void internalAdd(final Entry configuration)
    {
        assert configuration != null;

        // Obtain a write lock,
        lock.write(() ->
        {
            // add the configuration to the set of configurations,
            entries.add(configuration);

            // add it to the global lookup
            Registry.global().register(configuration.object(), configuration.identifier().instance());

            // then walk up the class hierarchy of the configuration object,
            var at = (Class<?>) configuration.object().getClass();
            final var instance = configuration.identifier().instance();
            while (!at.equals(Object.class))
            {
                // adding the configuration object for each superclass.
                final var identifier = new Entry.ConfigurationIdentifier(at, instance);
                identifierToConfiguration.put(identifier, configuration);
                at = at.getSuperclass();

                // Also put the configuration into the global lookup
            }
        });
    }

    @UmlExcludeMember
    protected void internalAddAll(final ConfigurationSet configurations)
    {
        lock.write(() -> configurations.asSet().forEach(this::internalAdd));
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
        final var properties = PropertyMap.load(resource);
        try
        {
            // then get the configuration class to instantiate,
            final var configurationClassName = properties.get("class");
            ensureNotNull(configurationClassName, "Missing class property in $", resource);
            final var configurationClass = Class.forName(configurationClassName);
            ensureNotNull(configurationClass, "Unable to load class $ specified in $", configurationClass, resource);
            trace("Configuration class: $", configurationClass.getSimpleName());

            // and the name of which identifier of the class to configure (if any)
            final var configurationInstance = properties.get("configuration-instance");
            final var identifier = configurationInstance != null ? new InstanceIdentifier(configurationInstance) : InstanceIdentifier.SINGLETON;
            trace("Configuration identifier: $", identifier);

            // then create the configuration object and populate it using the converter framework
            final var configuration = properties.asObject(this, configurationClass);
            trace("Loaded configuration: $", configuration);

            // and return the configuration set entry for the fully loaded configuration object
            return new Entry(new Entry.ConfigurationIdentifier(configurationClass, identifier), configuration);
        }
        catch (final Exception e)
        {
            return fail(e, "Unable to load properties from $", resource);
        }
    }

    /**
     * @return The set of loaded configurations
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
            return Sets.hashset(entries);
        });
    }

    /**
     * @return The configuration for the given identifier
     */
    private <T> T get(final Entry.ConfigurationIdentifier identifier)
    {
        return lock.read(() ->
        {
            final var configuration = identifierToConfiguration.get(identifier);
            return configuration == null ? null : configuration.object();
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
}
