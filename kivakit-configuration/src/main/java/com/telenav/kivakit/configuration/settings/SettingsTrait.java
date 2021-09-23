package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.lexakai.annotations.associations.UmlRelation;

public interface SettingsTrait extends Listener
{
    /**
     * @return True if this set has a settings object of the given type
     */
    default boolean hasSettings(final Class<?> type)
    {
        return settings(type) != null;
    }

    /**
     * @return True if this set has the specified instance of the settings object specified by the given type
     */
    default boolean hasSettings(final Class<?> type, final InstanceIdentifier instance)
    {
        return settings(type, instance) != null;
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

    default Settings registerAllSettingsIn(final Settings settings)
    {
        return settings().registerAllIn(settings);
    }

    default Settings registerAllSettingsIn(final Folder folder)
    {
        return settings().registerAllIn(listenTo(new SettingsFolder(folder)));
    }

    default Settings registerAllSettingsIn(final PackagePath path)
    {
        return registerAllSettingsIn(listenTo(SettingsPackage.of(path)));
    }

    default Settings registerAllSettingsIn(final Package package_)
    {
        return registerAllSettingsIn(listenTo(SettingsPackage.of(package_)));
    }

    default Settings registerAllSettingsIn(final Class<?> relativeTo, final String path)
    {
        return registerAllSettingsIn(PackagePath.parsePackagePath(relativeTo, path));
    }

    default Settings registerAllSettingsIn(final Class<?> type)
    {
        return registerAllSettingsIn(PackagePath.packagePath(type));
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
        return settings().registerSettings(settings, instance);
    }

    default Settings settings()
    {
        return Settings.of(this);
    }

    /**
     * @return The settings object of the given type
     */
    @UmlRelation(label = "gets values")
    default <T> T settings(final Class<T> type)
    {
        return settings().settings(type);
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    default <T> T settings(final Class<T> type, final InstanceIdentifier instance)
    {
        return settings().settings(type, instance);
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    default <T> T settings(final Class<T> type, final Enum<?> instance)
    {
        return settings().settings(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The settings object for the given type and instance identifier
     */
    default <T> T settings(final Class<T> type, final String instance)
    {
        return settings().settings(type, InstanceIdentifier.of(instance));
    }
}
