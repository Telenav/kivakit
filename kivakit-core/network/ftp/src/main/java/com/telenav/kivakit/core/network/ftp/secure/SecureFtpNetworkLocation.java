////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.ftp.secure;

import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.network.core.NetworkPath;
import com.telenav.kivakit.core.network.core.Protocol;
import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramSecureFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

@UmlClassDiagram(diagram = DiagramSecureFtp.class)
public class SecureFtpNetworkLocation extends NetworkLocation
{
    public SecureFtpNetworkLocation(final NetworkPath path)
    {
        super(path);
        ensure(Protocol.SFTP.equals(path.port().protocol()));
    }

    @UmlRelation(label = "creates")
    public SecureFtpResource resource(final NetworkAccessConstraints constraints)
    {
        return new SecureFtpResource(this, constraints);
    }
}
