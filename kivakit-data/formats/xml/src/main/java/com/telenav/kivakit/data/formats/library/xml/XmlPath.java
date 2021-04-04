////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.library.xml;

import com.telenav.kivakit.data.formats.library.xml.project.lexakai.diagrams.DiagramXml;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Extracts a string from a node.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramXml.class)
public abstract class XmlPath
{
    private final String name;

    protected XmlPath(final String name)
    {
        this.name = name;
    }

    public abstract String extract(XmlNode node);

    public String name()
    {
        return name;
    }
}
