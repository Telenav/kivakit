////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email.converters;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.network.core.EmailAddress;

public class EmailAddressConverter extends BaseStringConverter<EmailAddress>
{
    public EmailAddressConverter(final Listener listener)
    {
        super(listener);
    }

    @Override
    protected EmailAddress onConvertToObject(final String value)
    {
        return EmailAddress.parse(value);
    }
}
