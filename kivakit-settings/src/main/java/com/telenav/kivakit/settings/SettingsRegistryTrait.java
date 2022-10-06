package com.telenav.kivakit.settings;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.singletonInstanceIdentifier;

/**
 * <p>
 * A stateless trait for accessing the {@link SettingsRegistry} for the implementing component. Settings for a component can be
 * retrieved with {@link #settingsRegistry()}. This provides a simplified interface to load settings objects specified
 * by the user while also allowing for default settings when they are not specified. See {@link SettingsRegistry} for details.
 * </p>
 *
 * <p>
 * Includes methods to look up, register and require settings objects. When there is more than one settings object of a
 * given type, an {@link InstanceIdentifier} can be given to distinguish between instances. Convenience methods allow
 * {@link Enum}s to be used as identifiers as well. The <i>Component</i>> interface in <i>kivakit-component</i> uses
 * this trait to add easy access to settings objects to all components.
 * </p>
 *
 * <p><b>Registry Access</b></p>
 *
 * <ul>
 *     <li>{@link #settingsRegistry()}</li>
 * </ul>
 *
 * <p><b>Register methods</b></p>
 *
 * <ul>
 *     <li>{@link #registerSettings(Object)} - Registers the given object</li>
 *     <li>{@link #registerSettings(Object, Enum)} - Registers the given identified object instance</li>
 *     <li>{@link #registerSettings(Object, InstanceIdentifier)} - Registers the given identified object instance</li>
 *     <li>{@link #registerSettingsIn(SettingsStore)} - Registers the settings in the given folder with this settings object</li>
 * </ul>
 *
 * <p><b>Lookup methods</b></p>
 *
 * <ul>
 *     <li>{@link #hasSettings(Class)} - Determines if the registered instance of the given class can be found</li>
 *     <li>{@link #hasSettings(Class, InstanceIdentifier)} - Determines if the registered instance of the given class can be found</li>
 *     <li>{@link #hasSettings(Class, Enum)} - Determines if the registered instance of the given class can be found</li>
 *     <li>{@link #lookupSettings(Class)} - Locates the registered instance of the given class</li>
 *     <li>{@link #lookupSettings(Class, Enum)} - Locates the specified registered instance of the given class</li>
 *     <li>{@link #lookupSettings(Class, InstanceIdentifier)} - Locates the specified registered instance of the given class</li>
 *     <li>{@link #lookupSettings(Class, InstanceIdentifier, ResourceFolder)} - Locates the specified registered instance of the given class, or loads it from the given folder</li>
 *     <li>{@link #requireSettings(Class)} - Locates the registered instance of the given class or fails</li>
 *     <li>{@link #requireSettings(Class, Enum)} - Locates the specified registered instance of the given class or fails</li>
 *     <li>{@link #requireSettings(Class, InstanceIdentifier)} - Locates the specified registered instance of the given class or fails</li>
 *     <li>{@link #settingsRegistry()} - The {@link SettingsRegistry} for this object</li>
 * </ul>
 *
 * <p><b>Loading</b></p>
 *
 * <ul>
 *     <li>{@link #settingsIn(SettingsStore)}</li>
 *     <li>{@link #unloadSettings()}</li>
 * </ul>
 *
 * <p><b>Saving</b></p>
 *
 * <ul>
 *     <li>{@link #saveSettings(SettingsStore, Object)}</li>
 *     <li>{@link #saveSettings(SettingsStore, Object, Enum)}</li>
 *     <li>{@link #saveSettings(SettingsStore, Object, InstanceIdentifier)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see SettingsRegistry
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface SettingsRegistryTrait extends Repeater
{
    /**
     * @return True if this set has a settings object of the given type
     */
    default boolean hasSettings(Class<?> type)
    {
        return lookupSettings(type) != null;
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(Class<?> type, InstanceIdentifier instance)
    {
        return lookupSettings(type, instance) != null;
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(Class<?> type, Enum<?> instance)
    {
        return hasSettings(type, InstanceIdentifier.instanceIdentifier(instance));
    }

    /**
     * @return The settings object of the given type
     */
    @UmlRelation(label = "gets values")
    default <T> T lookupSettings(Class<T> type)
    {
        return settingsRegistry().lookupSettings(type);
    }

    /**
     * @return The settings object for the given class and instance identifier. If the settings object can't be found,
     * the given default settings package is searched.
     */
    default <T> T lookupSettings(Class<T> settingsClass,
                                 InstanceIdentifier instance,
                                 ResourceFolder<?> defaultSettings)
    {
        return settingsRegistry().lookupSettings(settingsClass, instance, defaultSettings);
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    default <T> T lookupSettings(Class<T> type, InstanceIdentifier instance)
    {
        return settingsRegistry().lookupSettings(type, instance);
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    default <T> T lookupSettings(Class<T> type, Enum<?> instance)
    {
        return settingsRegistry().lookupSettings(type, InstanceIdentifier.instanceIdentifier(instance));
    }

    /**
     * @return Add the given settings object to this set
     */
    default SettingsRegistry registerSettings(Object settings)
    {
        return registerSettings(settings, singletonInstanceIdentifier());
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    default SettingsRegistry registerSettings(Object settings, Enum<?> instance)
    {
        return registerSettings(settings, InstanceIdentifier.instanceIdentifier(instance));
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    default SettingsRegistry registerSettings(Object settings, InstanceIdentifier instance)
    {
        return settingsRegistry().registerSettings(settings, instance);
    }

    /**
     * Adds the settings objects from the given {@link Deployment} to the settings registry for this component.
     */
    default SettingsRegistry registerSettingsIn(SettingsStore settings)
    {
        return settingsRegistry().registerSettingsIn(settings);
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(Class<T> type)
    {
        return requireSettings(type, singletonInstanceIdentifier());
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(Class<T> type, Enum<?> instance)
    {
        return requireSettings(type, InstanceIdentifier.instanceIdentifier(instance));
    }

    /**
     * @return The object of the given instance and type, or {@link Ensure#fail()} if there is no such object
     */
    default <T> T requireSettings(Class<T> type, InstanceIdentifier instance)
    {
        return ensureNotNull(lookupSettings(type, instance), "Cannot find settings object: $:$", type, instance);
    }

    /**
     * Saves the given instance of the given object to the given settings store
     *
     * @param store The store to save to
     * @param object The object
     * @param instance Which instance of the object
     * @return True if the object was saved
     */
    default boolean saveSettings(SettingsStore store, Object object, InstanceIdentifier instance)
    {
        return store.save(new SettingsObject(object, instance));
    }

    /**
     * Saves the given instance of the given object to the given settings store
     *
     * @param store The store to save to
     * @param object The object
     * @param instance Which instance of the object
     * @return True if the object was saved
     */
    default boolean saveSettings(SettingsStore store, Object object, Enum<?> instance)
    {
        return saveSettings(store, object, InstanceIdentifier.instanceIdentifier(instance));
    }

    /**
     * Saves the given object to the given settings store
     *
     * @param store The store to save to
     * @param object The object
     * @return True if the object was saved
     */
    default boolean saveSettings(SettingsStore store, Object object)
    {
        return saveSettings(store, object, singletonInstanceIdentifier());
    }

    /**
     * Returns the settings objects in the given store
     *
     * @return The settings in the given store
     */
    default ObjectSet<SettingsObject> settingsIn(SettingsStore store)
    {
        return store.indexed();
    }

    default SettingsRegistry settingsRegistry()
    {
        return SettingsRegistry.settingsRegistryFor(this);
    }

    /**
     * @return True if all settings were cleared
     */
    default boolean unloadSettings()
    {
        return settingsRegistry().unload();
    }
}
