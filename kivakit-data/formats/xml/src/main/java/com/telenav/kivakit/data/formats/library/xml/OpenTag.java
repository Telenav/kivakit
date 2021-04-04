////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
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
