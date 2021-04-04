////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.library.xml;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.data.formats.library.xml.project.lexakai.diagrams.DiagramXml;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramXml.class)
public class OpenTag implements Matcher<XmlNode>
{
    private final String name;

    public OpenTag(final String name)
    {
        this.name = name;
    }

    @Override
    public boolean matches(final XmlNode node)
    {
        return node.isOpenTag() && node.name().equals(name);
    }
}
