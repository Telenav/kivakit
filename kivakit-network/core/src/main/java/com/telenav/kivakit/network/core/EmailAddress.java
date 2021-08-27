////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.network.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.regex.Pattern;

/**
 * An email address, according to RFC 5322.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "An email address",
        example = "jonathanl@telenav.com")
@LexakaiJavadoc(complete = true)
public class EmailAddress
{
    // See RFC 5322
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    /**
     * @return An email address for the given string, or null if the string is not an email address
     */
    public static EmailAddress parse(final String email)
    {
        assert email != null;

        if (EMAIL_PATTERN.matcher(email).matches())
        {
            return new EmailAddress(email);
        }
        return null;
    }

    /**
     * Converts to and from an {@link EmailAddress}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<EmailAddress>
    {
        protected Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected EmailAddress onToValue(final String value)
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
