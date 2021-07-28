package com.telenav.kivakit.application.component;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;

import static com.telenav.kivakit.configuration.lookup.InstanceIdentifier.SINGLETON;

/**
 * Interface to KivaKit component functionality, including easy access to settings (see {@link Settings}) and object
 * registries (see {@link Registry}).
 *
 * <p><b>Settings Registry Methods</b></p>
 *
 * <p>
 * Settings for a component can be retrieved with {@link #settings(Class)}. This provides a simplified interface to load
 * settings objects specified by the user while also allowing for default settings when they are not specified. See
 * {@link Settings} for details.
 * </p>
 *
 * <ul>
 *     <li>{@link #require(Class)} - Gets the given settings object or fails</li>
 *     <li>{@link #require(Class, Enum)} - Gets the settings object of the given instance or fails</li>
 *     <li>{@link #require(Class, String)} - Gets the settings object of the given instance or fails</li>
 *     <li>{@link #require(Class, InstanceIdentifier)} - Gets the settings object of the given instance or fails</li>
 *     <li>{@link #settings(Class)} - A settings object of the specified class or null if none exists</li>
 *     <li>{@link #settings(Class, Enum)} - Get any given instance of the given settings object type</li>
 *     <li>{@link #settings(Class, String)} - Get any given instance of the given settings object type</li>
 *     <li>{@link #settings(Class, InstanceIdentifier)} - Get any given instance of the given settings object type</li>
 *     <li>{@link #settingsRegistry()} - Retrieves the {@link Settings} registry for this component</li>
 *     <li>{@link #registerDeployment(Deployment)} - Adds the settings objects for the given deployment</li>
 *     <li>{@link #registerSettings(Settings)} - Adds the settings objects in the given settings registry</li>
 *     <li>{@link #registerSettingsObject(Object)}  - Adds the given settings object</li>
 *     <li>{@link #registerSettingsObject(Object, InstanceIdentifier)} - Adds the given settings object under the given instance identifier</li>
 *     <li>{@link #registerSettingsIn(Folder)} - Adds settings objects from the given folder</li>
 *     <li>{@link #registerSettingsIn(Package)} - Adds settings objects from the given package</li>
 * </ul>
 *
 * <p><b>Object Lookup Registry</b></p>
 *
 * <p>
 * Access to this component's lookup {@link Registry} is provided and fulfills basic needs for object wiring:
 * </p>
 *
 * <ul>
 *     <li>{@link #lookup(Class)} - Lookup the registered object of the given type, if any</li>
 *     <li>{@link #lookup(Class, String)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #lookup(Class, Enum)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #lookup(Class, InstanceIdentifier)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #registerObject(Object)} - Register the given singleton object for lookup</li>
 *     <li>{@link #registerObject(Object, String)} - Register the given instance of the given object for lookup</li>
 *     <li>{@link #registerObject(Object, Enum)} - Register the given instance of the given object for lookup</li>
 *     <li>{@link #registerObject(Object, InstanceIdentifier)} - Register the given instance of the given object for lookup</li>
 *     <li>{@link #registry()} - Retrieves the lookup {@link Registry} for this component</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Registry
 * @see Settings
 * @see Repeater
 */
public interface Component extends Repeater
{
    /**
     * Convenience method
     */
    default <T> T lookup(final Class<T> type)
    {
        return lookup(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T lookup(final Class<T> type, final Enum<?> instance)
    {
        return lookup(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T lookup(final Class<T> type, final String instance)
    {
        return lookup(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return Any settings object of the given type and instance
     */
    <T> T lookup(Class<T> type, InstanceIdentifier instance);

    default Resource packageResource(final String path)
    {
        return PackageResource.of(getClass(), path);
    }

    /**
     * Adds the settings objects from the given {@link Deployment} to the settings registry for this component.
     */
    default void registerDeployment(final Deployment deployment)
    {
        registerSettings(deployment);
    }

    /**
     * Convenience method
     */
    default <T> T registerObject(final T object)
    {
        return registerObject(object, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T registerObject(final T object, final Enum<?> instance)
    {
        return registerObject(object, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T registerObject(final T object, final String instance)
    {
        return registerObject(object, InstanceIdentifier.of(instance));
    }

    /**
     * Registers the given object and instance in the global object {@link Registry}
     */
    <T> T registerObject(T object, InstanceIdentifier instance);

    /**
     * Registers the settings from the given settings registry with the settings registry for this component
     */
    void registerSettings(Settings settings);

    /**
     * Registers the settings in the given folder with the settings registry for this component
     */
    void registerSettingsIn(Folder folder);

    /**
     * Registers the settings in the given package with the registry for this component
     */
    void registerSettingsIn(Package package_);

    /**
     * Convenience method
     */
    default void registerSettingsObject(final Object settings)
    {
        registerSettingsObject(settings, SINGLETON);
    }

    /**
     * Registers the given instance of the given settings object with the lookup registry for this component
     */
    void registerSettingsObject(Object settings, InstanceIdentifier instance);

    /**
     * @return The lookup registry for this component
     */
    default Registry registry()
    {
        return Registry.of(this);
    }

    /**
     * Convenience method
     */
    default <T> T require(final Class<T> type)
    {
        return require(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T require(final Class<T> type, final Enum<?> instance)
    {
        return require(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T require(final Class<T> type, final String instance)
    {
        return require(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The settings object of the given instance and type, or {@link Ensure#fail()} is called if there is no
     * such object
     */
    <T> T require(Class<T> type, InstanceIdentifier instance);

    /**
     * Convenience method
     */
    default <T> T settings(final Class<T> type)
    {
        return settings(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T settings(final Class<T> type, final Enum<?> instance)
    {
        return settings(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T settings(final Class<T> type, final String instance)
    {
        return settings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The settings object of the given instance and type or null if there is no such object
     */
    <T> T settings(Class<T> type, InstanceIdentifier instance);

    /**
     * @return The settings registry for this component
     */
    default Settings settingsRegistry()
    {
        return Settings.of(this);
    }
}
