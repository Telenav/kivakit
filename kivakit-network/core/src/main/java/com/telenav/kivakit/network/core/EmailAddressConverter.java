package com.telenav.kivakit.network.core;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Converts to and from an {@link EmailAddress}
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class EmailAddressConverter extends BaseStringConverter<EmailAddress>
{
    protected EmailAddressConverter(Listener listener)
    {
        super(listener, EmailAddress.class, EmailAddress::parseEmailAddress);
    }
}
