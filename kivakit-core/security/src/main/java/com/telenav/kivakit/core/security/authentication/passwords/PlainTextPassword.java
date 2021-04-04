////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
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
