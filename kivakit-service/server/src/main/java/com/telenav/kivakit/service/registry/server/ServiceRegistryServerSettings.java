package com.telenav.kivakit.service.registry.server;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Frequency;

/**
 * @author jonathanl (shibo)
 */
public class ServiceRegistryServerSettings
{
    /** The speed at which the Wicket web application auto-updates service information */
    public static final Frequency WICKET_AJAX_REFRESH_FREQUENCY = Frequency.every(Duration.seconds(10));
}
