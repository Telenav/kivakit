package com.telenav.kivakit.settings;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.packages.Package;
import com.telenav.kivakit.settings.stores.ResourceFolderSettingsStore;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.instanceIdentifier;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.singleton;
import static com.telenav.kivakit.settings.SettingsRegistry.settingsFor;

/**
 * <p>
 * A stateless trait for accessing the {@link SettingsRegistry} for the implementing component. Settings for a component
 * can be retrieved with {@link #settingsForThis()}. This provides a simplified interface to load settings objects
 * specified by the user while also allowing for default settings when they are not specified. See
 * {@link SettingsRegistry} for details.
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
 *     <li>{@link #settingsForThis()}</li>
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
 *     <li>{@link #settingsForThis()} - The {@link SettingsRegistry} for this object</li>
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
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface SettingsTrait extends Repeater
{
    /**
     * Returns true if this set has a settings object of the given type
     */
    default boolean hasSettings(Class<?> type)
    {
        return lookupSettings(type) != null;
    }

    /**
     * Returns true if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(Class<?> type, InstanceIdentifier instance)
    {
        return lookupSettings(type, instance) != null;
    }

    /**
     * Returns true if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(Class<?> type, Enum<?> instance)
    {
        return hasSettings(type, instanceIdentifier(instance));
    }

    /**
     * Returns the settings object of the given type
     */
    @UmlRelation(label = "gets values")
    default <T> T lookupSettings(Class<T> type)
    {
        return settingsForThis().lookupSettings(type);
    }

    /**
     * Returns the settings object for the given class and instance identifier. If the settings object can't be found,
     * the given default settings package is searched.
     */
    default <T> T lookupSettings(Class<T> settingsClass,
                                 InstanceIdentifier instance,
                                 ResourceFolder<?> defaultSettings)
    {
        return settingsForThis().lookupSettings(settingsClass, instance, defaultSettings);
    }

    /**
     * Returns the settings object for the given type and instance identifier
     */
    default <T> T lookupSettings(Class<T> type, InstanceIdentifier instance)
    {
        return settingsForThis().lookupSettings(type, instance);
    }

    /**
     * Returns the settings object for the given type and instance identifier
     */
    default <T> T lookupSettings(Class<T> type, Enum<?> instance)
    {
        return settingsForThis().lookupSettings(type, instanceIdentifier(instance));
    }

    /**
     * Returns add the given settings object to this set
     */
    default SettingsRegistry registerSettings(Object settings)
    {
        return registerSettings(settings, singleton());
    }

    /**
     * Returns adds the given instance of a settings object to this set
     */
    default SettingsRegistry registerSettings(Object settings, Enum<?> instance)
    {
        return registerSettings(settings, instanceIdentifier(instance));
    }

    /**
     * Returns adds the given instance of a settings object to this set
     */
    default SettingsRegistry registerSettings(Object settings, InstanceIdentifier instance)
    {
        return settingsForThis().registerSettings(settings, instance);
    }

    /**
     * Adds the settings objects from the given {@link Deployment} to the settings registry for this component.
     */
    default SettingsRegistry registerSettingsIn(SettingsStore settings)
    {
        return settingsForThis().registerSettingsIn(settings);
    }

    /**
     * Registers the settings in the given package
     */
    default SettingsRegistry registerSettingsIn(Package _package)
    {
        return registerSettingsIn(listenTo(new ResourceFolderSettingsStore(this, _package)));
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(Class<T> type)
    {
        return requireSettings(type, singleton());
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(Class<T> type, Enum<?> instance)
    {
        return requireSettings(type, instanceIdentifier(instance));
    }

    /**
     * Returns the object of the given instance and type, or {@link Ensure#fail()} if there is no such object
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
        return saveSettings(store, object, instanceIdentifier(instance));
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
        return saveSettings(store, object, singleton());
    }

    /**
     * Returns the setting registry for this object. Normally, this is the global settings registry.
     */
    default SettingsRegistry settingsForThis()
    {
        return settingsFor(this);
    }

    /**
     * Returns the settings objects in the given store
     *
     * @return The settings in the given store
     */
    default ObjectSet<SettingsObject> settingsIn(SettingsStore store)
    {
        return store.objects();
    }

    /**
     * Returns true if all settings were cleared
     */
    default void unloadSettings()
    {
        settingsForThis().clearRegistry();
    }
}
