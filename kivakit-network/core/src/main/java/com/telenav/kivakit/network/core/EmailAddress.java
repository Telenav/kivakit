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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * An email address, according to RFC 5322.
 *
 * <b><p>Parsing</p></b>
 *
 * <ul>
 *     <li>{@link #parseEmailAddress(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #email()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class EmailAddress
{
    /** Email pattern. See RFC 5322 */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    /**
     * Parses the given text into an email address
     *
     * @return An email address for the given string, or null if the string is not an email address
     */
    public static EmailAddress parseEmailAddress(Listener listener, String text)
    {
        assert text != null;

        if (EMAIL_PATTERN.matcher(text).matches())
        {
            return new EmailAddress(text);
        }
        listener.warning("Invalid email address: $", text);
        return null;
    }

    /**
     * Converts to and from an {@link EmailAddress}
     *
     * @author jonathanl (shibo)
     */
    @ApiQuality(stability = STABLE,
                testing = TESTING_NOT_NEEDED,
                documentation = FULLY_DOCUMENTED)
    public static class Converter extends BaseStringConverter<EmailAddress>
    {
        protected Converter(Listener listener)
        {
            super(listener, EmailAddress::parseEmailAddress);
        }
    }

    /** The email address */
    private String email;

    protected EmailAddress(String email)
    {
        this.email = email;
    }

    protected EmailAddress()
    {
    }

    /**
     * Returns this email address as a string
     */
    public String email()
    {
        return email;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof EmailAddress)
        {
            var that = (EmailAddress) object;
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
