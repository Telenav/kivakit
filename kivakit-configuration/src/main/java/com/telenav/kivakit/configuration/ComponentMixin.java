package com.telenav.kivakit.configuration;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.language.mixin.Mixin;
import com.telenav.kivakit.kernel.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.resource.resources.packaged.Package;

/**
 * A mixin for {@link BaseComponent} which can be used by a class that already extends another base class.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Mixin
 */
public interface ComponentMixin extends Component, Mixin, RepeaterMixin
{
    /**
     * @return The {@link BaseComponent} implementation associated with this mixin
     */
    default BaseComponent component()
    {
        return state(ComponentMixin.class, BaseComponent::new);
    }

    @Override
    default <T> T lookup(final Class<T> type, final InstanceIdentifier instance)
    {
        return component().lookup(type, instance);
    }

    @Override
    default <T> T registerObject(final T object, final InstanceIdentifier instance)
    {
        return component().registerObject(object, instance);
    }

    @Override
    default void registerSettings(final Settings settings)
    {
        settingsRegistry().registerAll(settings);
    }

    @Override
    default void registerSettingsIn(final Folder folder)
    {
        settingsRegistry().registerAllIn(folder);
    }

    @Override
    default void registerSettingsIn(final Package package_)
    {
        settingsRegistry().registerAllIn(package_);
    }

    @Override
    default void registerSettingsObject(final Object settings, final InstanceIdentifier instance)
    {
        settingsRegistry().register(settings, instance);
    }

    @Override
    default <T> T require(final Class<T> type, final InstanceIdentifier instance)
    {
        return settingsRegistry().require(type, instance);
    }

    @Override
    default <T> T settings(final Class<T> type, final InstanceIdentifier instance)
    {
        return settingsRegistry().settings(type, instance);
    }
}
