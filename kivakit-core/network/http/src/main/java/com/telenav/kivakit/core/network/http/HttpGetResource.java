////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http;

import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

@UmlClassDiagram(diagram = DiagramHttp.class)
public class HttpGetResource extends BaseHttpResource
{
    public HttpGetResource(final NetworkLocation location, final NetworkAccessConstraints constraints)
    {
        super(location, constraints);
        ensure(location.port().isHttp());
    }

    @Override
    protected HttpUriRequest newRequest()
    {
        final var uri = asUri();
        final var request = new HttpGet(uri);
        onInitialize(request);
        return request;
    }

    /**
     * Method to allow super classes to add parameters / headers.
     *
     * @param get The get to be sent.
     */
    protected void onInitialize(final HttpGet get)
    {
    }
}
