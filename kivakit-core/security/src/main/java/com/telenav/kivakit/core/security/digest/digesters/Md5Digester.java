////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.digest.digesters;

import com.telenav.kivakit.core.security.digest.BaseDigester;
import com.telenav.kivakit.core.security.project.lexakai.diagrams.DiagramSecurityDigest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramSecurityDigest.class)
public class Md5Digester extends BaseDigester
{
    public Md5Digester()
    {
        super("MD5");
    }
}
