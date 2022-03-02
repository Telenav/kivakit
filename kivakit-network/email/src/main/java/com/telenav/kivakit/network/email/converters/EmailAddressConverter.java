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

package com.telenav.kivakit.network.email.converters;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converts to and from {@link EmailAddress} objects
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class EmailAddressConverter extends BaseStringConverter<EmailAddress>
{
    public EmailAddressConverter(Listener listener)
    {
        super(listener, EmailAddress::parseEmail);
    }
}
