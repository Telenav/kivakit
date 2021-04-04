////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.digest;

import com.telenav.kivakit.core.security.project.lexakai.diagrams.DiagramSecurityDigest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UmlClassDiagram(diagram = DiagramSecurityDigest.class)
public abstract class BaseDigester implements Digester
{
    private final String algorithmName;

    protected BaseDigester(final String algorithmName)
    {
        this.algorithmName = algorithmName;
    }

    @Override
    public byte[] digest(final byte[] value)
    {
        try
        {
            return MessageDigest.getInstance(algorithmName).digest(value);
        }
        catch (final NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("Can't create digest", e);
        }
    }
}
