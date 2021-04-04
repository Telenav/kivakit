////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http;

import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;

@UmlClassDiagram(diagram = DiagramHttp.class)
public class HttpAccessConstraints extends NetworkAccessConstraints
{
    @UmlAggregation
    private HttpBasicCredentials httpBasicCredentials;

    public HttpBasicCredentials httpBasicCredentials()
    {
        return httpBasicCredentials;
    }

    public void httpBasicCredentials(final HttpBasicCredentials httpBasicCredentials)
    {
        this.httpBasicCredentials = httpBasicCredentials;
    }
}
