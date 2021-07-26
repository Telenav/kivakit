package com.telenav.kivakit.configuration;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;

/**
 * Base class for KivaKit components. Provides easy access to object registration and lookup (see {@link Registry}) as
 * well as settings registration and lookup (see {@link Settings}).
 *
 * <p><b>Settings Registry</b></p>
 *
 * <p>
 * Settings for any configurable object can be retrieved with {@link #settings(Class)}, as described in {@link
 * Settings}. This provides a simplified interface to load settings objects specified by the user while also allowing
 * for default settings when they are not specified. See {@link Settings} for details.
 * </p>
 *
 * <ul>
 *     <li>{@link #settings()} - Retrieves the {@link Settings} registry for this component</li>
 *     <li>{@link #settings(Class)} - A settings object of the specified class</li>
 *     <li>{@link #settings(Class, InstanceIdentifier)} - Get the configuration object to configure the given instance</li>
 *     <li>{@link #require(Class)} - Gets the given configuration object or fails</li>
 *     <li>{@link #require(Class, InstanceIdentifier)} - Gets the configuration object for the given instance or fails</li>
 *     <li>{@link #addDeployment(Deployment)} - Adds the configuration objects for the given deployment</li>
 *     <li>{@link #addSettings(Object)}  - Adds the given configuration object</li>
 *     <li>{@link #addSettings(Object, InstanceIdentifier)} - Adds the configuration object to configure the given instance</li>
 * </ul>
 *
 * <p><b>Lookup Registry</b></p>
 *
 * <p>
 * Access to this component's lookup {@link Registry} is provided and fulfills basic needs for object wiring:
 * </p>
 *
 * <ul>
 *     <li>{@link #registry()} - Retrieves the lookup {@link Registry} for this component</li>
 *     <li>{@link #lookup(Class)} - Lookup the registered object of the given type, if any</li>
 *     <li>{@link #lookup(Class, String)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #lookup(Class, Enum)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #lookup(Class, InstanceIdentifier)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #register(Object)} - Register the given singleton object for lookup</li>
 *     <li>{@link #register(Object, String)} - Register the given instance of the given object for lookup</li>
 *     <li>{@link #register(Object, Enum)} - Register the given instance of the given object for lookup</li>
 *     <li>{@link #register(Object, InstanceIdentifier)} - Register the given instance of the given object for lookup</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public class BaseComponent extends BaseRepeater
{
    /**
     * Adds the set of settings objects from the given {@link Deployment} to the settings registry for this application.
     * By default, this is the global settings store.
     */
    public void addDeployment(final Deployment deployment)
    {
        settings().addDeployment(deployment);
    }

    /**
     * Adds the given settings object to the settings registry for this application. By default, this is the global
     * settings store.
     */
    public void addSettings(final Object settings)
    {
        settings().add(settings);
    }

    /**
     * Adds the identified settings object to the settings registry for this application. By default, this is the global
     * settings store.
     */
    public void addSettings(final Object configuration, final InstanceIdentifier instance)
    {
        settings().add(configuration, instance);
    }

    /**
     * @return Any registered object of the given type with the given instance identifier in the global lookup registry
     */
    public <T> T lookup(final Class<T> type, final String instance)
    {
        return registry().find(type, instance);
    }

    /**
     * @return Any registered object of the given type with the given instance identifier in the global lookup registry
     */
    public <T> T lookup(final Class<T> type, final InstanceIdentifier instance)
    {
        return registry().find(type, instance);
    }

    /**
     * @return Any registered object of the given type with the given instance identifier in the global lookup registry
     */
    public <T> T lookup(final Class<T> type, final Enum<?> instance)
    {
        return registry().find(type, instance);
    }

    /**
     * @return The object of the given type from the application's lookup {@link Registry}, if any.
     */
    public <T> T lookup(final Class<T> type)
    {
        return registry().find(type);
    }

    /**
     * Registers the given singleton object in the global lookup registry
     */
    public <T> T register(final T object)
    {
        return registry().add(object);
    }

    /**
     * Registers the specified instance of the given object's type in the global lookup registry
     */
    public <T> T register(final T object, final String instance)
    {
        return registry().add(object, instance);
    }

    /**
     * Registers the specified instance of the given object's type in the global lookup registry
     */
    public <T> T register(final T object, final InstanceIdentifier instance)
    {
        return registry().add(object, instance);
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    public <T> T register(final T object, final Enum<?> instance)
    {
        return registry().add(object, instance);
    }

    /**
     * @return The lookup registry for this application
     */
    public Registry registry()
    {
        return Registry.of(this);
    }

    /**
     * @return The configuration object of the given type or failure if it doesn't exist
     */
    public <T> T require(final Class<T> type)
    {
        return Settings.require(type);
    }

    /**
     * @return The configuration object of the given type to configure the given instance or failure if it doesn't exist
     */
    public <T> T require(final Class<T> type, final InstanceIdentifier instance)
    {
        return Settings.require(type, instance);
    }

    /**
     * @return The configuration object of the given type, if any exists
     */
    public <T> T settings(final Class<T> type)
    {
        return settings().settings(type);
    }

    /**
     * @return The configuration object of the given type for the given instance to be configured, if any exists
     */
    public <T> T settings(final Class<T> type, final InstanceIdentifier instance)
    {
        return settings().settings(type, instance);
    }

    /**
     * @return The settings registry for this application
     */
    public Settings settings()
    {
        return Settings.of(this);
    }
}
