////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http;

import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramHttp.class)
public class HttpStatus
{
    private final int code;

    public HttpStatus(final int code)
    {
        this.code = code;
    }

    public boolean isFailure()
    {
        return !isOkay();
    }

    public boolean isOkay()
    {
        return code >= 200 && code <= 300;
    }

    @Override
    public String toString()
    {
        return Integer.toString(code);
    }
}
