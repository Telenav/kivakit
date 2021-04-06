////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.regex.Pattern;

/**
 * An email address, according to RFC 5322.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "An email address",
        example = "jonathanl@telenav.com")
public class EmailAddress
{
    // See RFC 5322
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public static EmailAddress parse(final String email)
    {
        assert email != null;

        if (EMAIL_PATTERN.matcher(email).matches())
        {
            return new EmailAddress(email);
        }
        return null;
    }

    public static class Converter extends BaseStringConverter<EmailAddress>
    {
        protected Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected EmailAddress onConvertToObject(final String value)
        {
            return parse(value);
        }
    }

    @JsonProperty
    @Schema(description = "An SMTP email address of the form user@host",
            required = true)
    private String email;

    protected EmailAddress(final String email)
    {
        this.email = email;
    }

    protected EmailAddress()
    {
    }

    public String email()
    {
        return email;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof EmailAddress)
        {
            final var that = (EmailAddress) object;
            return email.equals(that.email);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return email.hashCode();
    }

    @Override
    public String toString()
    {
        return email;
    }
}
