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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.kivakit.network.core.authentication.Password;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramAuthentication;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A plain text password, which can be tested against a given password using {@link #matches(Password)}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramAuthentication.class)
@UmlExcludeSuperTypes({ StringFormattable.class })
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class PlainTextPassword implements Password, StringFormattable
{
    /**
     * Parses the given text into a plain text password
     *
     * @param listener The listener to notify with any problems
     * @param text The text to parse
     */
    public static PlainTextPassword parsePlainTextPassword(Listener listener, String text)
    {
        return new PlainTextPassword(text);
    }

    /**
     * Converts passwords to and from {@link Password} objects.
     *
     * @author jonathanl (shibo)
     */
    @ApiQuality(stability = API_STABLE_EXTENSIBLE,
                testing = TESTING_NONE,
                documentation = DOCUMENTATION_COMPLETE)
    public static class Converter extends BaseStringConverter<Password>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected Password onToValue(String value)
        {
            return PlainTextPassword.parsePlainTextPassword(this, value);
        }
    }

    private final String password;

    protected PlainTextPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String asString(@NotNull Format format)
    {
        if (password != null)
        {
            return AsciiArt.repeat(password.length(), '*');
        }
        return null;
    }

    @SuppressWarnings("SpellCheckingInspection")
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
