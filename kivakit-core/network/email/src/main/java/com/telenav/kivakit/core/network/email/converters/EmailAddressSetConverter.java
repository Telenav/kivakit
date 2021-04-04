////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email.converters;

import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseSetConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.network.core.EmailAddress;

public class EmailAddressSetConverter extends BaseSetConverter<EmailAddress>
{
    public EmailAddressSetConverter(final Listener listener)
    {
        super(listener, new EmailAddressConverter(listener), ",");
    }
}
