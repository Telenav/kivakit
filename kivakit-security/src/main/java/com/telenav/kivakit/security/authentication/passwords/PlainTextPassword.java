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

package com.telenav.kivakit.security.authentication.passwords;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.security.authentication.Password;
import com.telenav.kivakit.security.project.lexakai.diagrams.DiagramSecurity;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * A plain text password, which can be tested against a given password using {@link #matches(Password)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSecurity.class)
@UmlExcludeSuperTypes({ AsString.class })
@LexakaiJavadoc(complete = true)
public class PlainTextPassword implements Password, AsString
{
    public static PlainTextPassword parse(final String password)
    {
        return new PlainTextPassword(password);
    }

    /**
     * Converts passwords to and from {@link Password} objects.
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Password>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Password onToValue(final String value)
        {
            return parse(value);
        }
    }

    private final String password;

    protected PlainTextPassword(final String password)
    {
        this.password = password;
    }

    @Override
    public String asString(final StringFormat format)
    {
        if (password != null)
        {
            return AsciiArt.repeat(password.length(), '*');
        }
        return null;
    }

    @Override
    public boolean matches(final Password uncast)
    {
        if (uncast instanceof PlainTextPassword)
        {
            final var that = (PlainTextPassword) uncast;
            return password.equals(that.password);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return password;
    }
}
