////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http;

import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import org.apache.http.client.methods.HttpPut;

@UmlClassDiagram(diagram = DiagramHttp.class)
public class HttpPutResource extends BaseHttpResource
{
    public HttpPutResource(final NetworkLocation networkLocation, final NetworkAccessConstraints constraints)
    {
        super(networkLocation, constraints);
    }

    @Override
    protected HttpPut newRequest()
    {
        final var request = new HttpPut(asUri());
        onInitialize(request);
        return request;
    }

    /**
     * Method to allow super classes to add parameters / headers.
     *
     * @param post The post to be sent.
     */
    protected void onInitialize(final HttpPut post)
    {
    }
}
