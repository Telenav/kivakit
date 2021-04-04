////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.authentication.converters;

import com.telenav.kivakit.core.security.authentication.Password;
import com.telenav.kivakit.core.security.authentication.passwords.PlainTextPassword;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;

public class PlainTextPasswordConverter extends BaseStringConverter<Password>
{
    public PlainTextPasswordConverter(final Listener listener)
    {
        super(listener);
    }

    @Override
    protected Password onConvertToObject(final String value)
    {
        return new PlainTextPassword(value);
    }
}
