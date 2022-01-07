package com.telenav.kivakit.component;

import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.configuration.settings.SettingsTrait;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;

import java.util.function.Consumer;

import static com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

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
    /**
     * Call the consumer with any messages this component hears
     *
     * @param handler The handler to call
     */
    public void onMessage(Consumer<Message> handler)
    {
        addListener(handler::accept);
    }
}
