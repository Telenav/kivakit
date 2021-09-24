package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.configuration.lookup.InstanceIdentifier.SINGLETON;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * <p>
 * A stateless trait for accessing the {@link Settings} for the implementing component. Includes methods to look up,
 * register and require settings objects. When there is more than one settings object of a given type, an {@link
 * InstanceIdentifier} can be given to distinguish between instances. Convenience methods are provided to create
 * instance identifiers for {@link Enum}s and {@link String}s. The Component interface in kivakit-component uses this
 * trait to add easy access to registry methods to all components.
 * </p>
 *
 * <p><b>Lookup methods</b></p>
 * <ul>
 *     <li>{@link #settingsRegistry()} - The {@link Settings} for this object</li>
 *     <li>{@link #hasSettings(Class)} - Determines if the registered instance of the given class can be found</li>
 *     <li>{@link #hasSettings(Class, String)} - Determines if the registered instance of the given class can be found<</li>
 *     <li>{@link #hasSettings(Class, Enum)} - Determines if the registered instance of the given class can be found<</li>
 *     <li>{@link #lookupSettings(Class)} - Locates the registered instance of the given class</li>
 *     <li>{@link #lookupSettings(Class, String)} - Locates the specified registered instance of the given class</li>
 *     <li>{@link #lookupSettings(Class, Enum)} - Locates the specified registered instance of the given class</li>
 * </ul>
 *
 * <p><b>Register methods</b></p>
 * <ul>
 *     <li>{@link #registerSettings(Object)} - Registers the given object</li>
 *     <li>{@link #registerSettings(Object, String)} - Registers the given identified object instance</li>
 *     <li>{@link #registerSettings(Object, Enum)} - Registers the given identified object instance</li>
 *     <li>{@link #registerAllSettingsIn(Settings)} - Registers the settings in the given object with this settings object</li>
 *     <li>{@link #registerAllSettingsIn(Listener, Folder)} - Registers the settings in the given folder with this settings object</li>
 *     <li>{@link #registerAllSettingsIn(Listener, PackagePath)} - Registers the settings in the given package with this settings object</li>
 *     <li>{@link #registerAllSettingsIn(Listener, Package)} - Registers the settings in the given package with this settings object</li>
 *     <li>{@link #registerAllSettingsIn(Listener, Class)} - Registers the settings in the given class' package with this settings object</li>
 *     <li>{@link #registerAllSettingsIn(Listener, Class, String)} - Registers the settings in the given class-relative package with this settings object</li>
 * </ul>
 *
 * <p><b>Require methods</b></p>
 * <ul>
 *     <li>{@link #requireSettings(Class)} - Locates the registered instance of the given class or fails</li>
 *     <li>{@link #requireSettings(Class, String)} - Locates the specified registered instance of the given class or fails</li>
 *     <li>{@link #requireSettings(Class, Enum)} - Locates the specified registered instance of the given class or fails</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Settings
 */
public interface SettingsTrait
{
    /**
     * @return True if this set has a settings object of the given type
     */
    default boolean hasSettings(final Class<?> type)
    {
        return lookupSettings(type) != null;
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(final Class<?> type, final InstanceIdentifier instance)
    {
        return lookupSettings(type, instance) != null;
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(final Class<?> type, final Enum<?> instance)
    {
        return hasSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(final Class<?> type, final String instance)
    {
        return hasSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The settings object of the given type
     */
    @UmlRelation(label = "gets values")
    default <T> T lookupSettings(final Class<T> type)
    {
        return settingsRegistry().lookupSettings(type);
    }

    default <T> T lookupSettings(final Class<T> settingsClass,
                                 final PackagePath defaultSettingsPackage,
                                 final InstanceIdentifier identifier)
    {
        return settingsRegistry().lookupSettings(settingsClass, defaultSettingsPackage, identifier);
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    default <T> T lookupSettings(final Class<T> type, final InstanceIdentifier instance)
    {
        return settingsRegistry().lookupSettings(type, instance);
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    default <T> T lookupSettings(final Class<T> type, final Enum<?> instance)
    {
        return settingsRegistry().lookupSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    default <T> T lookupSettings(final Class<T> type, final String instance)
    {
        return settingsRegistry().lookupSettings(type, InstanceIdentifier.of(instance));
    }

    default Settings registerAllSettingsIn(final Settings settings)
    {
        return settingsRegistry().registerAllSettingsIn(settings);
    }

    default Settings registerAllSettingsIn(final Listener listener, final Folder folder)
    {
        return settingsRegistry().registerAllSettingsIn(listener.listenTo(new SettingsFolder(folder)));
    }

    default Settings registerAllSettingsIn(final Listener listener, final PackagePath path)
    {
        return registerAllSettingsIn(listener.listenTo(SettingsPackage.of(path)));
    }

    default Settings registerAllSettingsIn(final Listener listener, final Package package_)
    {
        return registerAllSettingsIn(listener.listenTo(SettingsPackage.of(package_)));
    }

    default Settings registerAllSettingsIn(final Listener listener, final Class<?> relativeTo, final String path)
    {
        return registerAllSettingsIn(listener, PackagePath.parsePackagePath(relativeTo, path));
    }

    default Settings registerAllSettingsIn(final Listener listener, final Class<?> type)
    {
        return registerAllSettingsIn(listener, PackagePath.packagePath(type));
    }

    /**
     * Adds the settings objects from the given {@link Deployment} to the settings registry for this component.
     */
    default Settings registerDeployment(final Deployment deployment)
    {
        return registerAllSettingsIn(deployment);
    }

    /**
     * @return Add the given settings object to this set
     */
    default Settings registerSettings(final Object settings)
    {
        return registerSettings(settings, InstanceIdentifier.SINGLETON);
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    default Settings registerSettings(final Object settings, final Enum<?> instance)
    {
        return registerSettings(settings, InstanceIdentifier.of(instance));
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    default Settings registerSettings(final Object settings, final String instance)
    {
        return registerSettings(settings, InstanceIdentifier.of(instance));
    }

    /**
     * @return Adds the given instance of a settings object to this set
     */
    default Settings registerSettings(final Object settings, final InstanceIdentifier instance)
    {
        return settingsRegistry().registerSettings(settings, instance);
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(final Class<T> type)
    {
        return requireSettings(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(final Class<T> type, final Enum<?> instance)
    {
        return requireSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T requireSettings(final Class<T> type, final String instance)
    {
        return requireSettings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The object of the given instance and type, or {@link Ensure#fail()} if there is no such object
     */
    default <T> T requireSettings(final Class<T> type, final InstanceIdentifier instance)
    {
        return ensureNotNull(lookupSettings(type, instance));
    }

    default Settings settingsRegistry()
    {
        return Settings.of(this);
    }
}
