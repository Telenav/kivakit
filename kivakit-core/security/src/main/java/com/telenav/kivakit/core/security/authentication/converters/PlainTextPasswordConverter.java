////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.authentication.converters;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.security.authentication.Password;
import com.telenav.kivakit.core.security.authentication.passwords.PlainTextPassword;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converts passwords to and from {@link Password} objects. {@value}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
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
