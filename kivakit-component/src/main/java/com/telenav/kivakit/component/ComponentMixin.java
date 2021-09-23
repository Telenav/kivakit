package com.telenav.kivakit.component;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.mixin.Mixin;
import com.telenav.kivakit.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.resource.resources.packaged.Package;

/**
 * A mixin for {@link BaseComponent} which can be used by a class that already extends another base class.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Mixin
 */
public interface ComponentMixin extends Component, Mixin
{
    @Override
    default void addListener(final Listener listener, final Filter<Transmittable> filter)
    {
        component().addListener(listener, filter);
    }

    @Override
    default void clearListeners()
    {
        component().clearListeners();
    }

    /**
     * @return The {@link BaseComponent} implementation associated with this mixin
     */
    default BaseComponent component()
    {
        return state(ComponentMixin.class, BaseComponent::new);
    }

    @Override
    default CodeContext debugCodeContext()
    {
        return component().debugCodeContext();
    }

    @Override
    default void debugCodeContext(final CodeContext context)
    {
        component().debugCodeContext(context);
    }

    @Override
    default <T extends Transmittable> T handle(final T message)
    {
        return component().handle(message);
    }

    @Override
    default boolean hasListeners()
    {
        return component().hasListeners();
    }

    @Override
    default <T> T lookup(final Class<T> type, final InstanceIdentifier instance)
    {
        return component().lookup(type, instance);
    }

    @Override
    default void messageSource(final Broadcaster parent)
    {
        component().messageSource(parent);
    }

    @Override
    default Broadcaster messageSource()
    {
        return component().messageSource();
    }

    default String objectName()
    {
        return component().objectName();
    }

    default void objectName(String objectName)
    {
        component().objectName(objectName);
    }

    @Override
    default void onMessage(final Message message)
    {
        component().onMessage(message);
    }

    @Override
    default void registerAllSettings(final Settings settings)
    {
        settingsRegistry().registerAllIn(settings);
    }

    @Override
    default void registerAllSettingsIn(final Folder folder)
    {
        settingsRegistry().registerAllIn(folder);
    }

    @Override
    default void registerAllSettingsIn(final Package package_)
    {
        settingsRegistry().registerAllIn(package_);
    }

    @Override
    default <T> T registerObject(final T object, final InstanceIdentifier instance)
    {
        return component().registerObject(object, instance);
    }

    @Override
    default void registerSettingsObject(final Object settings, final InstanceIdentifier instance)
    {
        settingsRegistry().register(settings, instance);
    }

    @Override
    default void removeListener(final Listener listener)
    {
        component().removeListener(listener);
    }

    @Override
    default <T> T require(final Class<T> type, final InstanceIdentifier instance)
    {
        return component().require(type, instance);
    }

    @Override
    default <T> T settings(final Class<T> type, final InstanceIdentifier instance)
    {
        return settingsRegistry().settings(type, instance);
    }

    @Override
    default void transmit(final Message message)
    {
        component().transmit(message);
    }
}
