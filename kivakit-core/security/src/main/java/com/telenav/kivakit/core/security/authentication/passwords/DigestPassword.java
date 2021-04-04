////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.authentication.passwords;

import com.telenav.kivakit.core.security.project.lexakai.diagrams.DiagramSecurity;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Base64;

@UmlClassDiagram(diagram = DiagramSecurity.class)
public class DigestPassword extends PlainTextPassword
{
    public DigestPassword(final String password)
    {
        super(Base64.getEncoder().encodeToString(password.getBytes()));
    }
}
