package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.configuration.lookup.InstanceIdentifier.SINGLETON;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * <p>
 * A stateless trait for accessing the {@link Settings} for the implementing component.
 * </p>
 *
 * <p>
 * Includes methods to look up, register and require settings objects. When there is more than one settings object of a
 * given type, an {@link InstanceIdentifier} can be given to distinguish between instances. Convenience methods allow
 * {@link Enum}s to be used as identifiers as well. The <i>Component</i>> interface in <i>kivakit-component</i> uses
 * this trait to add easy access to settings objects to all components.
 * </p>
 *
 * <p><b>Register methods</b></p>
 *
 * <ul>
 *     <li>{@link #registerSettingsIn(SettingsStore)} - Registers the settings in the given folder with this settings object</li>
 *     <li>{@link #registerSettingsObject(Object)} - Registers the given object</li>
 *     <li>{@link #registerSettingsObject(Object, String)} - Registers the given identified object instance</li>
 *     <li>{@link #registerSettingsObject(Object, Enum)} - Registers the given identified object instance</li>
 * </ul>
 *
 * <p><b>Lookup methods</b></p>
 *
 * <ul>
 *     <li>{@link #settingsRegistry()} - The {@link Settings} for this object</li>
 *     <li>{@link #hasSettings(Class)} - Determines if the registered instance of the given class can be found</li>
 *     <li>{@link #hasSettings(Class, String)} - Determines if the registered instance of the given class can be found</li>
 *     <li>{@link #hasSettings(Class, Enum)} - Determines if the registered instance of the given class can be found</li>
 *     <li>{@link #lookupSettings(Class)} - Locates the registered instance of the given class</li>
 *     <li>{@link #lookupSettings(Class, String)} - Locates the specified registered instance of the given class</li>
 *     <li>{@link #lookupSettings(Class, Enum)} - Locates the specified registered instance of the given class</li>
 *     <li>{@link #requireSettings(Class)} - Locates the registered instance of the given class or fails</li>
 *     <li>{@link #requireSettings(Class, String)} - Locates the specified registered instance of the given class or fails</li>
 *     <li>{@link #requireSettings(Class, Enum)} - Locates the specified registered instance of the given class or fails</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Settings
 */
public interface SettingsTrait extends Repeater
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
        return hasSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(Class<?> type, String instance)
    {
        return hasSettings(type, InstanceIdentifier.of(instance));
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
                                 PackagePath defaultSettingsPackage)
    {
        return settingsRegistry().lookupSettings(settingsClass, instance, defaultSettingsPackage);
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
        return settingsRegistry().lookupSettings(type, InstanceIdentifier.of(instance));
    }

    default <T> T lookupSettings(Class<T> type, String instance)
    {
        return settingsRegistry().lookupSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * Adds the settings objects from the given {@link Deployment} to the settings registry for this component.
     */
    default Settings registerSettingsIn(SettingsStore settings)
    {
        return settingsRegistry().registerSettingsIn(settings);
    }

    /**
     * @return Add the given settings object to this set
     */
    default Settings registerSettingsObject(Object settings)
    {
        return registerSettingsObject(settings, InstanceIdentifier.SINGLETON);
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    default Settings registerSettingsObject(Object settings, Enum<?> instance)
    {
        return registerSettingsObject(settings, InstanceIdentifier.of(instance));
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    default Settings registerSettingsObject(Object settings, String instance)
    {
        return registerSettingsObject(settings, InstanceIdentifier.of(instance));
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    default Settings registerSettingsObject(Object settings, InstanceIdentifier instance)
    {
        return settingsRegistry().registerSettingsObject(settings, instance);
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(Class<T> type)
    {
        return requireSettings(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(Class<T> type, Enum<?> instance)
    {
        return requireSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(Class<T> type, String instance)
    {
        return requireSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The object of the given instance and type, or {@link Ensure#fail()} if there is no such object
     */
    default <T> T requireSettings(Class<T> type, InstanceIdentifier instance)
    {
        return ensureNotNull(lookupSettings(type, instance));
    }

    /**
     * Saves the given instance of the given object to the given settings store
     *
     * @param store The store to save to
     * @param object The object
     * @param instance Which instance of the object
     * @return True if the object was saved
     */
    default boolean saveSettingsTo(SettingsStore store, Object object, InstanceIdentifier instance)
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
    default boolean saveSettingsTo(SettingsStore store, Object object, Enum<?> instance)
    {
        return saveSettingsTo(store, object, InstanceIdentifier.of(instance));
    }

    /**
     * Saves the given instance of the given object to the given settings store
     *
     * @param store The store to save to
     * @param object The object
     * @param instance Which instance of the object
     * @return True if the object was saved
     */
    default boolean saveSettingsTo(SettingsStore store, Object object, String instance)
    {
        return saveSettingsTo(store, object, InstanceIdentifier.of(instance));
    }

    /**
     * Saves the given object to the given settings store
     *
     * @param store The store to save to
     * @param object The object
     * @return True if the object was saved
     */
    default boolean saveSettingsTo(SettingsStore store, Object object)
    {
        return saveSettingsTo(store, object, SINGLETON);
    }

    /**
     * @return The settings in the given store
     */
    default ObjectSet<SettingsObject> settingsIn(SettingsStore store)
    {
        return store.indexedSettingsObjects();
    }

    default Settings settingsRegistry()
    {
        return Settings.of(this);
    }

    /**
     * @return True if all settings were cleared
     */
    default boolean unloadSettings()
    {
        return settingsRegistry().unload();
    }
}
