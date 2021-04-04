////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.authentication;

import com.telenav.kivakit.core.security.project.lexakai.diagrams.DiagramSecurity;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.values.name.Name;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramSecurity.class)
public class UserName extends Name
{
    public static class Converter extends BaseStringConverter<UserName>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected UserName onConvertToObject(final String value)
        {
            return new UserName(value);
        }
    }

    public UserName(final String name)
    {
        super(name);
    }
}
