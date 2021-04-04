////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http.secure;

import com.telenav.kivakit.core.network.core.NetworkPath;
import com.telenav.kivakit.core.network.http.HttpNetworkLocation;
import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttps;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.network.core.Protocol.HTTPS;

@UmlClassDiagram(diagram = DiagramHttps.class)
@UmlRelation(label = "creates", referent = SecureHttpGetResource.class)
@UmlRelation(label = "creates", referent = SecureHttpPostResource.class)
public class SecureHttpNetworkLocation extends HttpNetworkLocation
{
    public SecureHttpNetworkLocation(final NetworkPath path)
    {
        super(path);
        ensure(HTTPS.equals(path.port().protocol()));
    }
}
