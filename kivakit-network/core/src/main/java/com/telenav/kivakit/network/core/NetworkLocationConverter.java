package com.telenav.kivakit.network.core;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converts to and from a {@link NetworkLocation}
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class NetworkLocationConverter extends BaseStringConverter<NetworkLocation>
{
    public NetworkLocationConverter(Listener listener)
    {
        super(listener, NetworkLocation.class, NetworkLocation::parseNetworkLocation);
    }
}
