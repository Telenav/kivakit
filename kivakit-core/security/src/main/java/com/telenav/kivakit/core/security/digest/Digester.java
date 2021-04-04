////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.digest;

import com.telenav.kivakit.core.security.project.lexakai.diagrams.DiagramSecurityDigest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramSecurityDigest.class)
public interface Digester
{
    /**
     * @param value The value to create a digest of
     * @return The digest
     */
    byte[] digest(final byte[] value);
}
