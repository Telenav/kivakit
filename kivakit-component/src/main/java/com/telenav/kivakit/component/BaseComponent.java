package com.telenav.kivakit.component;

import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.configuration.settings.SettingsTrait;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;

import java.util.function.Consumer;

/**
 * Base class for KivaKit components. Provides easy access to object registration and lookup (see {@link Registry}) as
 * well as settings registration and lookup (see {@link Settings}). For details, see {@link Component}.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Registry
 * @see Settings
 */
public class BaseComponent extends BaseRepeater implements Component, RegistryTrait, SettingsTrait
{
    /** The name of this object for debugging purposes */
    private String objectName;

    @Override
    public String objectName()
    {
        return objectName;
    }

    @Override
    public void objectName(final String objectName)
    {
        this.objectName = objectName;
    }

    /**
     * Call the consumer with any messages this component hears
     *
     * @param handler The handler to call
     */
    public void onMessage(final Consumer<Message> handler)
    {
        final Listener listener = handler::accept;
        listener.listenTo(this);
    }
}
