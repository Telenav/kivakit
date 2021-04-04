////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http.secure;

import com.telenav.kivakit.core.network.http.HttpGetResource;
import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttps;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
@UmlClassDiagram(diagram = DiagramHttps.class)
public class SecureHttpGetResource extends HttpGetResource
{
    private boolean ignoreInvalidCertificates;

    public SecureHttpGetResource(final SecureHttpNetworkLocation location, final NetworkAccessConstraints constraints)
    {
        super(location, constraints);
    }

    public void ignoreInvalidCertificates(final boolean ignore)
    {
        ignoreInvalidCertificates = ignore;
    }

    @Override
    protected DefaultHttpClient newClient()
    {
        return ignoreInvalidCertificates ? new InvalidCertificateTrustingHttpClient() : new DefaultHttpClient();
    }
}
