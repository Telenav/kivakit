////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
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
