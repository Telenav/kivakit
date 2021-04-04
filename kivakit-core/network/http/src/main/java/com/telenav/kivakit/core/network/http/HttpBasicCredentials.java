////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http;

import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.security.authentication.Password;
import com.telenav.kivakit.core.security.authentication.UserName;

@UmlClassDiagram(diagram = DiagramHttp.class)
public class HttpBasicCredentials
{
    private final Password password;

    private final UserName username;

    public HttpBasicCredentials(final UserName username, final Password password)
    {
        this.username = username;
        this.password = password;
    }

    public Password password()
    {
        return password;
    }

    @Override
    public String toString()
    {
        return username.toString();
    }

    public UserName userName()
    {
        return username;
    }
}
