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

package com.telenav.kivakit.core.security.authentication.passwords;

import com.telenav.kivakit.core.security.authentication.Password;
import com.telenav.kivakit.core.security.project.lexakai.diagrams.DiagramSecurity;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.kivakit.core.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;

@UmlClassDiagram(diagram = DiagramSecurity.class)
@UmlExcludeSuperTypes({ AsString.class })
public class PlainTextPassword implements Password, AsString
{
    private final String password;

    public PlainTextPassword(final String password)
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
    public boolean matches(final Password object)
    {
        if (object instanceof PlainTextPassword)
        {
            final var that = (PlainTextPassword) object;
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
