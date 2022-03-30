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

package com.telenav.kivakit.network.core.authentication.passwords;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.network.core.authentication.Password;
import com.telenav.kivakit.network.core.lexakai.DiagramAuthentication;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * A plain text password, which can be tested against a given password using {@link #matches(Password)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramAuthentication.class)
@UmlExcludeSuperTypes({ Stringable.class })
@LexakaiJavadoc(complete = true)
public class PlainTextPassword implements Password, Stringable
{
    public static PlainTextPassword parse(Listener listener, String password)
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
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected Password onToValue(String value)
        {
            return parse(this, value);
        }
    }

    private final String password;

    protected PlainTextPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String asString(Format format)
    {
        if (password != null)
        {
            return AsciiArt.repeat(password.length(), '*');
        }
        return null;
    }

    @Override
    public boolean matches(Password uncast)
    {
        if (uncast instanceof PlainTextPassword)
        {
            var that = (PlainTextPassword) uncast;
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
