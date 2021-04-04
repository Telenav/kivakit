////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.ftp;

import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.network.core.NetworkPath;
import com.telenav.kivakit.core.network.core.Protocol;
import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

@UmlClassDiagram(diagram = DiagramFtp.class)
public class FtpNetworkLocation extends NetworkLocation
{
    @UmlClassDiagram(diagram = DiagramFtp.class)
    public enum Mode
    {
        Active,
        Passive
    }

    @UmlAggregation(label = "transfers in mode")
    private Mode mode;

    public FtpNetworkLocation(final NetworkPath path)
    {
        super(path);
        ensure(Protocol.FTP.equals(path.port().protocol()));
    }

    public Mode mode()
    {
        return mode;
    }

    public void mode(final Mode mode)
    {
        this.mode = mode;
    }

    @UmlRelation(label = "creates")
    public FtpResource resource(final NetworkAccessConstraints constraints)
    {
        return new FtpResource(this, constraints);
    }
}
