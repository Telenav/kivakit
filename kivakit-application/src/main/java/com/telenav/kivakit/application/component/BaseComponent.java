package com.telenav.kivakit.application.component;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.resources.packaged.Package;

/**
 * Base class for KivaKit components. Provides easy access to object registration and lookup (see {@link Registry}) as
 * well as settings registration and lookup (see {@link Settings}). For details, see {@link Component}.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Registry
 * @see Settings
 */
public class BaseComponent extends BaseRepeater implements Component
{
    /**
     * @return Any registered object of the given type with the given instance identifier in the global lookup registry
     */
    @Override
    public <T> T lookup(final Class<T> type, final InstanceIdentifier instance)
    {
        return registry().lookup(type, instance);
    }

    /**
     * Registers the specified instance of the given object's type in the global lookup registry
     */
    @Override
    public <T> T registerObject(final T object, final InstanceIdentifier instance)
    {
        return registry().register(object, instance);
    }

    @Override
    public void registerSettings(final Settings settings)
    {
        settingsRegistry().registerAll(settings);
    }

    @Override
    public void registerSettingsIn(final Folder folder)
    {
        settingsRegistry().registerAllIn(folder);
    }

    @Override
    public void registerSettingsIn(final Package package_)
    {
        settingsRegistry().registerAllIn(package_);
    }

    /**
     * Adds the identified settings object to the settings registry for this application. By default, this is the global
     * settings store.
     */
    @Override
    public void registerSettingsObject(final Object settings, final InstanceIdentifier instance)
    {
        settingsRegistry().register(settings, instance);
    }

    /**
     * @return The configuration object of the given type to configure the given instance or failure if it doesn't exist
     */
    @Override
    public <T> T require(final Class<T> type, final InstanceIdentifier instance)
    {
        return settingsRegistry().require(type, instance);
    }

    /**
     * @return The configuration object of the given type for the given instance to be configured, if any exists
     */
    @Override
    public <T> T settings(final Class<T> type, final InstanceIdentifier instance)
    {
        return settingsRegistry().settings(type, instance);
    }
}
