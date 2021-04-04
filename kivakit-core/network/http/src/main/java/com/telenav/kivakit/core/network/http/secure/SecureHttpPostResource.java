////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http.secure;

import com.telenav.kivakit.core.network.http.HttpPostResource;
import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttps;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
@UmlClassDiagram(diagram = DiagramHttps.class)
public class SecureHttpPostResource extends HttpPostResource
{
    private boolean ignoreInvalidCertificates;

    public SecureHttpPostResource(final NetworkLocation networkLocation, final NetworkAccessConstraints constraints)
    {
        super(networkLocation, constraints);
        if (!(networkLocation instanceof SecureHttpNetworkLocation))
        {
            throw new IllegalArgumentException(
                    "Secure post request must use a secure network location:  " + networkLocation);
        }
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
